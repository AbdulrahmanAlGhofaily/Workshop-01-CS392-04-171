package com.pushwoosh.inbox.ui.presentation.presenter;

import android.os.Bundle;
import com.pushwoosh.inbox.PushwooshInbox;
import com.pushwoosh.inbox.data.InboxMessage;
import com.pushwoosh.inbox.event.InboxMessagesUpdatedEvent;
import com.pushwoosh.inbox.ui.OnInboxMessageClickListener;
import com.pushwoosh.inbox.ui.PushwooshInboxStyle;
import com.pushwoosh.inbox.ui.PushwooshInboxUi;
import com.pushwoosh.inbox.ui.model.repository.InboxEvent;
import com.pushwoosh.inbox.ui.model.repository.InboxRepository;
import com.pushwoosh.inbox.ui.presentation.data.UserError;
import com.pushwoosh.internal.event.Subscription;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class InboxPresenter extends BasePresenter {
    public static final Companion Companion = new Companion(null);
    private static final String KEY_SWIPE_REFRESH;
    private InboxEvent inboxEvent;
    private final WeakReference<InboxView> inboxViewRef;
    private final ArrayList<InboxMessage> messageList = new ArrayList<>();
    private Subscription<InboxMessagesUpdatedEvent> subscription;
    private boolean swipeToRefresh;

    public InboxPresenter(InboxView inboxView) {
        Intrinsics.checkParameterIsNotNull(inboxView, "inboxView");
        this.inboxViewRef = new WeakReference<>(inboxView);
    }

    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private final Function1<InboxEvent, Unit> getCallback() {
        return new InboxPresenter$callback$1(this);
    }

    @Override // com.pushwoosh.inbox.ui.presentation.presenter.BasePresenter, com.pushwoosh.inbox.ui.presentation.lifecycle.LifecycleListener
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.subscription = InboxRepository.INSTANCE.subscribeToEvent();
        InboxRepository.INSTANCE.addCallback(getCallback());
        this.inboxEvent = new InboxEvent.OnCreate();
    }

    @Override // com.pushwoosh.inbox.ui.presentation.presenter.BasePresenter
    public void restoreState(Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(bundle, "bundle");
        this.swipeToRefresh = bundle.getBoolean(KEY_SWIPE_REFRESH, this.swipeToRefresh);
        this.inboxEvent = new InboxEvent.RestoreState();
        implementState();
    }

    @Override // com.pushwoosh.inbox.ui.presentation.presenter.BasePresenter, com.pushwoosh.inbox.ui.presentation.lifecycle.LifecycleListener
    public void onViewCreated() {
        InboxView inboxView;
        super.onViewCreated();
        if (!this.messageList.isEmpty() && getRestore() && (inboxView = this.inboxViewRef.get()) != null) {
            inboxView.showList(this.messageList);
        }
        implementState();
    }

    /* access modifiers changed from: public */
    private final void implementState() {
        if (getViewEnable()) {
            InboxEvent inboxEvent2 = this.inboxEvent;
            if (inboxEvent2 instanceof InboxEvent.OnCreate) {
                loadInboxMessages();
            } else if (inboxEvent2 instanceof InboxEvent.Loading) {
                if (!this.swipeToRefresh) {
                    InboxView inboxView = this.inboxViewRef.get();
                    if (inboxView != null) {
                        inboxView.showTotalProgress();
                        return;
                    }
                    return;
                }
                InboxView inboxView2 = this.inboxViewRef.get();
                if (inboxView2 != null) {
                    inboxView2.showSwipeRefreshProgress();
                }
            } else if (inboxEvent2 instanceof InboxEvent.FinishLoading) {
                InboxView inboxView3 = this.inboxViewRef.get();
                if (inboxView3 != null) {
                    inboxView3.hideProgress();
                }
            } else if (inboxEvent2 instanceof InboxEvent.FailedLoading) {
                InboxView inboxView4 = this.inboxViewRef.get();
                if (inboxView4 != null) {
                    inboxView4.failedLoadingInboxList(new UserError(null, PushwooshInboxStyle.INSTANCE.getListErrorMessage(), 1, null));
                }
                this.swipeToRefresh = false;
            } else if (inboxEvent2 instanceof InboxEvent.SuccessLoadingCache) {
                this.messageList.clear();
                this.messageList.addAll(((InboxEvent.SuccessLoadingCache) inboxEvent2).getInboxMessages());
                showList();
                InboxRepository.INSTANCE.loadInbox(!getRestore(), null, -1);
            } else if (inboxEvent2 instanceof InboxEvent.SuccessLoading) {
                this.messageList.clear();
                this.messageList.addAll(((InboxEvent.SuccessLoading) inboxEvent2).getInboxMessages());
                showList();
            } else if (inboxEvent2 instanceof InboxEvent.InboxEmpty) {
                this.messageList.clear();
                InboxView inboxView5 = this.inboxViewRef.get();
                if (inboxView5 != null) {
                    inboxView5.showEmptyView();
                }
            } else if (inboxEvent2 instanceof InboxEvent.InboxMessagesUpdated) {
                InboxEvent.InboxMessagesUpdated inboxMessagesUpdated = (InboxEvent.InboxMessagesUpdated) inboxEvent2;
                this.messageList.removeAll(inboxMessagesUpdated.getDeleted());
                this.messageList.addAll(inboxMessagesUpdated.getAddedInboxMessages());
                for (T t : inboxMessagesUpdated.getUpdatedInboxMessages()) {
                    if (this.messageList.contains(t)) {
                        ArrayList<InboxMessage> arrayList = this.messageList;
                        arrayList.set(arrayList.indexOf(t), t);
                    } else {
                        this.messageList.add(t);
                    }
                }
                showList();
            }
        }
    }

    private final void loadInboxMessages() {
        this.messageList.clear();
        Collection<InboxMessage> loadCachedInbox = InboxRepository.INSTANCE.loadCachedInbox(null, 40);
        if (!loadCachedInbox.isEmpty()) {
            this.messageList.addAll(loadCachedInbox);
        }
        showList();
        InboxRepository.INSTANCE.loadCachedInboxAsync(null, -1);
    }

    private final void showList() {
        Collections.sort(this.messageList, InboxPresenter$showList$1.INSTANCE);
        InboxView inboxView = this.inboxViewRef.get();
        if (inboxView != null) {
            inboxView.showList(this.messageList);
        }
    }

    @Override // com.pushwoosh.inbox.ui.presentation.presenter.BasePresenter, com.pushwoosh.inbox.ui.presentation.lifecycle.LifecycleListener
    public void onSaveInstanceState(Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(bundle, "out");
        bundle.putBoolean(KEY_SWIPE_REFRESH, this.swipeToRefresh);
    }

    @Override // com.pushwoosh.inbox.ui.presentation.presenter.BasePresenter, com.pushwoosh.inbox.ui.presentation.lifecycle.LifecycleListener
    public void onDestroy(boolean z) {
        Subscription<InboxMessagesUpdatedEvent> subscription2 = this.subscription;
        if (subscription2 != null) {
            subscription2.unsubscribe();
        }
        InboxRepository.INSTANCE.removeCallback(getCallback());
    }

    public final void removeItem(InboxMessage inboxMessage) {
        if (inboxMessage != null) {
            this.messageList.remove(inboxMessage);
            InboxRepository.INSTANCE.removeItem(inboxMessage);
        }
    }

    public final void refreshItems() {
        this.swipeToRefresh = true;
        InboxRepository.INSTANCE.loadInbox(true, null, -1);
    }

    public final void onItemClick(InboxMessage inboxMessage) {
        Intrinsics.checkParameterIsNotNull(inboxMessage, "inboxMessage");
        PushwooshInbox.performAction(inboxMessage.getCode());
        OnInboxMessageClickListener onMessageClickListener = PushwooshInboxUi.INSTANCE.getOnMessageClickListener();
        if (onMessageClickListener != null) {
            onMessageClickListener.onInboxMessageClick(inboxMessage);
        }
    }
}
