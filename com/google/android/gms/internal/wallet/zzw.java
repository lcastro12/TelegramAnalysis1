package com.google.android.gms.internal.wallet;

import android.annotation.SuppressLint;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.Payments;

@SuppressLint({"MissingRemoteException"})
public final class zzw implements Payments {
    public final PendingResult<BooleanResult> isReadyToPay(GoogleApiClient googleApiClient) {
        return googleApiClient.enqueue(new zzab(this, googleApiClient));
    }

    public final void loadFullWallet(GoogleApiClient googleApiClient, FullWalletRequest fullWalletRequest, int i) {
        googleApiClient.enqueue(new zzz(this, googleApiClient, fullWalletRequest, i));
    }
}
