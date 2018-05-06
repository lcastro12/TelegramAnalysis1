package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.TL_auth_exportAuthorization;
import org.telegram.TL.TLRPC.TL_auth_exportedAuthorization;
import org.telegram.TL.TLRPC.TL_auth_importAuthorization;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;

public class ExportAuthorizationAction extends Action {
    public Datacenter datacenter;
    TL_auth_exportedAuthorization exportedAuthorization;
    int retryCount;

    class C08441 implements RPCRequestDelegate {

        class C03071 implements Runnable {
            C03071() {
            }

            public void run() {
                ExportAuthorizationAction.this.beginExport();
            }
        }

        C08441() {
        }

        public void run(TLObject response, TL_error error) {
            if (ExportAuthorizationAction.this.delegate != null) {
                if (error == null) {
                    ExportAuthorizationAction.this.exportedAuthorization = (TL_auth_exportedAuthorization) response;
                    ExportAuthorizationAction.this.beginImport();
                    return;
                }
                ExportAuthorizationAction exportAuthorizationAction = ExportAuthorizationAction.this;
                exportAuthorizationAction.retryCount++;
                if (ExportAuthorizationAction.this.retryCount >= 3) {
                    ExportAuthorizationAction.this.delegate.ActionDidFailExecution(ExportAuthorizationAction.this);
                } else {
                    Utilities.stageQueue.postRunnable(new C03071(), ExportAuthorizationAction.this.retryCount * 1500);
                }
            }
        }
    }

    class C08452 implements RPCRequestDelegate {

        class C03081 implements Runnable {
            C03081() {
            }

            public void run() {
                ExportAuthorizationAction.this.beginExport();
            }
        }

        C08452() {
        }

        public void run(TLObject response, TL_error error) {
            if (ExportAuthorizationAction.this.delegate != null) {
                if (error == null) {
                    ExportAuthorizationAction.this.delegate.ActionDidFinishExecution(ExportAuthorizationAction.this, null);
                    return;
                }
                ExportAuthorizationAction.this.exportedAuthorization = null;
                ExportAuthorizationAction exportAuthorizationAction = ExportAuthorizationAction.this;
                exportAuthorizationAction.retryCount++;
                if (ExportAuthorizationAction.this.retryCount >= 3) {
                    ExportAuthorizationAction.this.delegate.ActionDidFailExecution(ExportAuthorizationAction.this);
                } else {
                    Utilities.stageQueue.postRunnable(new C03081(), ExportAuthorizationAction.this.retryCount * 1500);
                }
            }
        }
    }

    public ExportAuthorizationAction(Datacenter d) {
        this.datacenter = d;
    }

    public void execute(HashMap options) {
        if (this.datacenter == null) {
            this.delegate.ActionDidFailExecution(this);
        } else {
            beginExport();
        }
    }

    void beginExport() {
        TL_auth_exportAuthorization exportAuthorization = new TL_auth_exportAuthorization();
        exportAuthorization.dc_id = this.datacenter.datacenterId;
        ConnectionsManager.Instance.performRpc(exportAuthorization, new C08441(), null, true, RPCRequest.RPCRequestClassGeneric);
    }

    void beginImport() {
        TL_auth_importAuthorization importAuthorization = new TL_auth_importAuthorization();
        importAuthorization.bytes = this.exportedAuthorization.bytes;
        importAuthorization.id = this.exportedAuthorization.id;
        ConnectionsManager.Instance.performRpc(importAuthorization, new C08452(), null, true, RPCRequest.RPCRequestClassGeneric | RPCRequest.RPCRequestClassEnableUnauthorized, this.datacenter.datacenterId);
    }
}
