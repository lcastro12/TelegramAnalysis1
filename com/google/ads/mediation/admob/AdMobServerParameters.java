package com.google.ads.mediation.admob;

import com.google.ads.mediation.MediationServerParameters;

public final class AdMobServerParameters extends MediationServerParameters {
    @Parameter(name = "pubid")
    public String adUnitId;
    @Parameter(name = "mad_hac", required = false)
    public String allowHouseAds = null;
    public int tagForChildDirectedTreatment = -1;
}
