package com.google.android.gms.measurement.internal;

final class zzgd implements Runnable {
    private final /* synthetic */ zzn zzpg;
    private final /* synthetic */ zzfk zzph;

    zzgd(zzfk zzfk, zzn zzn) {
        this.zzph = zzfk;
        this.zzpg = zzn;
    }

    public final void run() {
        zzfk.zza(this.zzph).zzjq();
        zzfk.zza(this.zzph).zzf(this.zzpg);
    }
}
