package com.google.android.gms.measurement.internal;

final class zzfu implements Runnable {
    private final /* synthetic */ zzn zzpg;
    private final /* synthetic */ zzfk zzph;

    zzfu(zzfk zzfk, zzn zzn) {
        this.zzph = zzfk;
        this.zzpg = zzn;
    }

    public final void run() {
        zzfk.zza(this.zzph).zzjq();
        zzfk.zza(this.zzph).zzd(this.zzpg);
    }
}
