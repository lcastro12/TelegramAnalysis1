package org.telegram.messenger.exoplayer2.upstream;

import android.net.Uri;
import java.io.IOException;
import org.telegram.messenger.exoplayer2.upstream.DataSource.Factory;

public final class DummyDataSource implements DataSource {
    public static final Factory FACTORY = new C07051();
    public static final DummyDataSource INSTANCE = new DummyDataSource();

    static class C07051 implements Factory {
        C07051() {
        }

        public DataSource createDataSource() {
            return new DummyDataSource();
        }
    }

    private DummyDataSource() {
    }

    public long open(DataSpec dataSpec) throws IOException {
        throw new IOException("Dummy source");
    }

    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        throw new UnsupportedOperationException();
    }

    public Uri getUri() {
        return null;
    }

    public void close() throws IOException {
    }
}
