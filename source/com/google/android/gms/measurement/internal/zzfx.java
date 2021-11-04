package com.google.android.gms.measurement.internal;

final class zzfx implements Runnable {
    private final /* synthetic */ zzai zzdm;
    private final /* synthetic */ zzn zzpg;
    private final /* synthetic */ zzfk zzph;

    zzfx(zzfk zzfk, zzai zzai, zzn zzn) {
        this.zzph = zzfk;
        this.zzdm = zzai;
        this.zzpg = zzn;
    }

    public final void run() {
        zzai zzb = this.zzph.zzb(this.zzdm, this.zzpg);
        zzfk.zza(this.zzph).zzjq();
        zzfk.zza(this.zzph).zzc(zzb, this.zzpg);
    }
}
