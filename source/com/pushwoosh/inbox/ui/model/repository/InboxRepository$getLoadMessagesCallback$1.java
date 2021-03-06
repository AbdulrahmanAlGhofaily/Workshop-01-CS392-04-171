package com.pushwoosh.inbox.ui.model.repository;

import com.pushwoosh.exception.PushwooshException;
import com.pushwoosh.function.Callback;
import com.pushwoosh.function.Result;
import com.pushwoosh.inbox.data.InboxMessage;
import com.pushwoosh.inbox.exception.InboxMessagesException;
import com.pushwoosh.inbox.ui.model.repository.InboxEvent;
import com.pushwoosh.internal.utils.PWLog;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012.\u0010\u0002\u001a*\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\u0005 \u0006*\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00040\u0004\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00070\u00070\u0003H\n¢\u0006\u0002\b\b"}, d2 = {"<anonymous>", "", "result", "Lcom/pushwoosh/function/Result;", "", "Lcom/pushwoosh/inbox/data/InboxMessage;", "kotlin.jvm.PlatformType", "Lcom/pushwoosh/inbox/exception/InboxMessagesException;", "process"}, k = 3, mv = {1, 1, 15})
/* compiled from: InboxRepository.kt */
final class InboxRepository$getLoadMessagesCallback$1<T, E extends PushwooshException> implements Callback<Collection<? extends InboxMessage>, InboxMessagesException> {
    final /* synthetic */ boolean $isLoadingCachedMessages;

    InboxRepository$getLoadMessagesCallback$1(boolean z) {
        this.$isLoadingCachedMessages = z;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [com.pushwoosh.function.Result<java.util.Collection<com.pushwoosh.inbox.data.InboxMessage>, com.pushwoosh.inbox.exception.InboxMessagesException>] */
    @Override // com.pushwoosh.function.Callback
    public final void process(@NotNull Result<Collection<? extends InboxMessage>, InboxMessagesException> result) {
        Intrinsics.checkParameterIsNotNull(result, "result");
        PWLog.noise("loadInbox", "result isSuccess: " + result.isSuccess());
        InboxRepository.access$updateEvent(InboxRepository.INSTANCE, new InboxEvent.FinishLoading());
        Collection<? extends InboxMessage> data = result.getData();
        InboxRepository.access$getCurrentInboxMessages$p(InboxRepository.INSTANCE).clear();
        if (data != null) {
            InboxRepository.access$getCurrentInboxMessages$p(InboxRepository.INSTANCE).addAll(new ArrayList(data));
        }
        if (data != null && (!data.isEmpty())) {
            if (this.$isLoadingCachedMessages) {
                InboxRepository.access$updateEvent(InboxRepository.INSTANCE, new InboxEvent.SuccessLoadingCache(InboxRepository.access$getCurrentInboxMessages$p(InboxRepository.INSTANCE)));
            } else {
                InboxRepository.access$updateEvent(InboxRepository.INSTANCE, new InboxEvent.SuccessLoading(InboxRepository.access$getCurrentInboxMessages$p(InboxRepository.INSTANCE)));
            }
        }
        InboxMessagesException exception = result.getException();
        if (exception != null) {
            InboxRepository.access$updateEvent(InboxRepository.INSTANCE, new InboxEvent.FailedLoading(exception));
        }
        if (exception != null) {
            return;
        }
        if (data == null || data.isEmpty()) {
            InboxRepository.access$updateEvent(InboxRepository.INSTANCE, new InboxEvent.InboxEmpty());
        }
    }
}
