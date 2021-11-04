package com.google.android.gms.measurement.internal;

final class zzfy implements Runnable {
    private final /* synthetic */ zzn zzpg;
    private final /* synthetic */ zzfk zzph;
    private final /* synthetic */ zzjn zzpi;

    zzfy(zzfk zzfk, zzjn zzjn, zzn zzn) {
        this.zzph = zzfk;
        this.zzpi = zzjn;
        this.zzpg = zzn;
    }

    public final void run() {
        zzfk.zza(this.zzph).zzjq();
        zzfk.zza(this.zzph).zzc(this.zzpi, this.zzpg);
    }
}
