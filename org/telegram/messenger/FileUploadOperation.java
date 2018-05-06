package org.telegram.messenger;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Locale;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.InputEncryptedFile;
import org.telegram.TL.TLRPC.InputFile;
import org.telegram.TL.TLRPC.TL_boolTrue;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.TL_inputEncryptedFileBigUploaded;
import org.telegram.TL.TLRPC.TL_inputEncryptedFileUploaded;
import org.telegram.TL.TLRPC.TL_inputFile;
import org.telegram.TL.TLRPC.TL_inputFileBig;
import org.telegram.TL.TLRPC.TL_upload_saveBigFilePart;
import org.telegram.TL.TLRPC.TL_upload_saveFilePart;
import org.telegram.messenger.RPCRequest.RPCProgressDelegate;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;

public class FileUploadOperation {
    private long currentFileId;
    private int currentPartNum = 0;
    private long currentUploaded = 0;
    public FileUploadOperationDelegate delegate;
    private int fingerprint;
    private boolean isBigFile = false;
    private boolean isLastPart = false;
    private byte[] iv;
    private byte[] key;
    MessageDigest mdEnc = null;
    private byte[] readBuffer;
    private long requestToken = 0;
    public int state = 0;
    FileInputStream stream;
    private long totalFileSize = 0;
    private int totalPartsCount = 0;
    private int uploadChunkSize = 32768;
    private String uploadingFilePath;

    public interface FileUploadOperationDelegate {
        void didChangedUploadProgress(FileUploadOperation fileUploadOperation, float f);

        void didFailedUploadingFile(FileUploadOperation fileUploadOperation);

        void didFinishUploadingFile(FileUploadOperation fileUploadOperation, InputFile inputFile, InputEncryptedFile inputEncryptedFile);
    }

    class C08501 implements RPCRequestDelegate {
        C08501() {
        }

        public void run(TLObject response, TL_error error) {
            FileUploadOperation.this.requestToken = 0;
            if (error != null) {
                FileUploadOperation.this.delegate.didFailedUploadingFile(FileUploadOperation.this);
            } else if (response instanceof TL_boolTrue) {
                FileUploadOperation.this.currentPartNum = FileUploadOperation.this.currentPartNum + 1;
                FileUploadOperation.this.delegate.didChangedUploadProgress(FileUploadOperation.this, ((float) FileUploadOperation.this.currentUploaded) / ((float) FileUploadOperation.this.totalFileSize));
                if (FileUploadOperation.this.isLastPart) {
                    FileUploadOperation.this.state = 3;
                    if (FileUploadOperation.this.key == null) {
                        InputFile result;
                        if (FileUploadOperation.this.isBigFile) {
                            result = new TL_inputFileBig();
                        } else {
                            result = new TL_inputFile();
                            result.md5_checksum = String.format(Locale.US, "%32s", new Object[]{new BigInteger(1, FileUploadOperation.this.mdEnc.digest()).toString(16)}).replace(' ', '0');
                        }
                        result.parts = FileUploadOperation.this.currentPartNum;
                        result.id = FileUploadOperation.this.currentFileId;
                        result.name = FileUploadOperation.this.uploadingFilePath.substring(FileUploadOperation.this.uploadingFilePath.lastIndexOf("/") + 1);
                        FileUploadOperation.this.delegate.didFinishUploadingFile(FileUploadOperation.this, result, null);
                        return;
                    }
                    InputEncryptedFile result2;
                    if (FileUploadOperation.this.isBigFile) {
                        result2 = new TL_inputEncryptedFileBigUploaded();
                    } else {
                        result2 = new TL_inputEncryptedFileUploaded();
                        result2.md5_checksum = String.format(Locale.US, "%32s", new Object[]{new BigInteger(1, FileUploadOperation.this.mdEnc.digest()).toString(16)}).replace(' ', '0');
                    }
                    result2.parts = FileUploadOperation.this.currentPartNum;
                    result2.id = FileUploadOperation.this.currentFileId;
                    result2.key_fingerprint = FileUploadOperation.this.fingerprint;
                    FileUploadOperation.this.delegate.didFinishUploadingFile(FileUploadOperation.this, null, result2);
                    return;
                }
                FileUploadOperation.this.startUploadRequest();
            } else {
                FileUploadOperation.this.delegate.didFailedUploadingFile(FileUploadOperation.this);
            }
        }
    }

    class C08512 implements RPCProgressDelegate {
        C08512() {
        }

        public void progress(int length, int progress) {
        }
    }

    public FileUploadOperation(String location, byte[] keyarr, byte[] ivarr) {
        this.uploadingFilePath = location;
        if (!(ivarr == null || keyarr == null)) {
            this.iv = new byte[32];
            this.key = keyarr;
            System.arraycopy(ivarr, 0, this.iv, 0, 32);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] arr = new byte[64];
                System.arraycopy(this.key, 0, arr, 0, 32);
                System.arraycopy(this.iv, 0, arr, 32, 32);
                byte[] digest = md.digest(arr);
                byte[] fingerprintBytes = new byte[4];
                for (int a = 0; a < 4; a++) {
                    fingerprintBytes[a] = (byte) (digest[a] ^ digest[a + 4]);
                }
                this.fingerprint = Utilities.bytesToInt(fingerprintBytes);
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            }
        }
        this.currentFileId = (long) (MessagesController.random.nextDouble() * 9.223372036854776E18d);
        try {
            this.mdEnc = MessageDigest.getInstance("MD5");
        } catch (Exception e2) {
            FileLog.m799e("tmessages", e2);
        }
    }

    public void start() {
        if (this.state == 0) {
            this.state = 1;
            startUploadRequest();
        }
    }

    public void cancel() {
        if (this.state == 1) {
            this.state = 2;
            if (this.requestToken != 0) {
                ConnectionsManager.Instance.cancelRpc(this.requestToken, true);
            }
            this.delegate.didFailedUploadingFile(this);
        }
    }

    private void startUploadRequest() {
        if (this.state == 1) {
            try {
                TLObject finalRequest;
                if (this.stream == null) {
                    File cacheFile = new File(this.uploadingFilePath);
                    this.stream = new FileInputStream(cacheFile);
                    this.totalFileSize = cacheFile.length();
                    if (this.totalFileSize > 10485760) {
                        FileLog.m800e("tmessages", "file is big!");
                        this.isBigFile = true;
                    }
                    this.uploadChunkSize = (int) Math.max(32.0d, Math.ceil((double) (((float) this.totalFileSize) / 3072000.0f)));
                    if (1024 % this.uploadChunkSize != 0) {
                        int chunkSize = 64;
                        while (this.uploadChunkSize > chunkSize) {
                            chunkSize *= 2;
                        }
                        this.uploadChunkSize = chunkSize;
                    }
                    this.uploadChunkSize *= 1024;
                    this.totalPartsCount = (int) Math.ceil((double) (((float) this.totalFileSize) / ((float) this.uploadChunkSize)));
                    this.readBuffer = new byte[this.uploadChunkSize];
                }
                int readed = this.stream.read(this.readBuffer);
                int toAdd = 0;
                if (!(this.key == null || readed % 16 == 0)) {
                    toAdd = 0 + (16 - (readed % 16));
                }
                byte[] sendBuffer = new byte[(readed + toAdd)];
                if (readed != this.uploadChunkSize) {
                    this.isLastPart = true;
                }
                System.arraycopy(this.readBuffer, 0, sendBuffer, 0, readed);
                if (this.key != null) {
                    sendBuffer = Utilities.aesIgeEncryption(sendBuffer, this.key, this.iv, true, true);
                }
                this.mdEnc.update(sendBuffer, 0, readed + toAdd);
                TLObject req;
                if (this.isBigFile) {
                    req = new TL_upload_saveBigFilePart();
                    req.file_part = this.currentPartNum;
                    req.file_id = this.currentFileId;
                    req.file_total_parts = this.totalPartsCount;
                    req.bytes = sendBuffer;
                    finalRequest = req;
                } else {
                    req = new TL_upload_saveFilePart();
                    req.file_part = this.currentPartNum;
                    req.file_id = this.currentFileId;
                    req.bytes = sendBuffer;
                    finalRequest = req;
                }
                this.currentUploaded += (long) readed;
                this.requestToken = ConnectionsManager.Instance.performRpc(finalRequest, new C08501(), new C08512(), null, true, RPCRequest.RPCRequestClassUploadMedia, Integer.MAX_VALUE);
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                this.delegate.didFailedUploadingFile(this);
            }
        }
    }
}
