package com.pushwoosh.inbox.ui.model.repository;

import com.pushwoosh.function.Callback;
import com.pushwoosh.inbox.PushwooshInbox;
import com.pushwoosh.inbox.data.InboxMessage;
import com.pushwoosh.inbox.event.InboxMessagesUpdatedEvent;
import com.pushwoosh.inbox.exception.InboxMessagesException;
import com.pushwoosh.inbox.ui.model.repository.InboxEvent;
import com.pushwoosh.internal.event.EventBus;
import com.pushwoosh.internal.event.Subscription;
import com.pushwoosh.internal.utils.PWLog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public final class InboxRepository {
    public static final InboxRepository INSTANCE = new InboxRepository();
    private static final List<Function1<InboxEvent, Unit>> callbacks = new ArrayList();
    private static InboxEvent currentInboxEvent;
    private static final List<InboxMessage> currentInboxMessages = new ArrayList();

    private InboxRepository() {
    }

    public final void addCallback(Function1<? super InboxEvent, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "callback");
        callbacks.add(function1);
        InboxEvent inboxEvent = currentInboxEvent;
        if (inboxEvent != null) {
            if (inboxEvent == null) {
                Intrinsics.throwNpe();
            }
            function1.invoke(inboxEvent);
        }
    }

    public final void removeCallback(Function1<? super InboxEvent, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "callback");
        callbacks.remove(function1);
    }

    public final Subscription<InboxMessagesUpdatedEvent> subscribeToEvent() {
        Subscription<InboxMessagesUpdatedEvent> subscribe = EventBus.subscribe(InboxMessagesUpdatedEvent.class, InboxRepository$subscribeToEvent$1.INSTANCE);
        Intrinsics.checkExpressionValueIsNotNull(subscribe, "EventBus.subscribe(Inbox… deleted))\n            })");
        return subscribe;
    }

    public final void loadInbox(boolean z, InboxMessage inboxMessage, int i) {
        updateEvent(new InboxEvent.Loading());
        PushwooshInbox.loadMessages(getLoadMessagesCallback(false), inboxMessage, i);
    }

    public final Collection<InboxMessage> loadCachedInbox(InboxMessage inboxMessage, int i) {
        Collection<InboxMessage> loadCachedMessages = PushwooshInbox.loadCachedMessages(inboxMessage, i);
        Intrinsics.checkExpressionValueIsNotNull(loadCachedMessages, "PushwooshInbox.loadCache…ages(inboxMessage, limit)");
        return loadCachedMessages;
    }

    public final void loadCachedInboxAsync(InboxMessage inboxMessage, int i) {
        PushwooshInbox.loadCachedMessages(getLoadMessagesCallback(true), inboxMessage, i);
    }

    private final Callback<Collection<InboxMessage>, InboxMessagesException> getLoadMessagesCallback(boolean z) {
        return new InboxRepository$getLoadMessagesCallback$1(z);
    }

    /* access modifiers changed from: public */
    private final void updateEvent(InboxEvent inboxEvent) {
        PWLog.noise("updateEvent", "InboxEvent: " + inboxEvent.getClass().getName());
        currentInboxEvent = inboxEvent;
        Iterator<T> it = callbacks.iterator();
        while (it.hasNext()) {
            it.next().invoke(inboxEvent);
        }
    }

    public final void removeItem(InboxMessage inboxMessage) {
        Intrinsics.checkParameterIsNotNull(inboxMessage, "inboxMessage");
        PushwooshInbox.deleteMessage(inboxMessage.getCode());
    }
}
