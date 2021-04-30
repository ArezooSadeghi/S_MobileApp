package com.example.sipmobileapp.viewmodel;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private AtomicBoolean mIsPending = new AtomicBoolean(false);

    private static final String TAG = SingleLiveEvent.class.getSimpleName();

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (hasObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                if (mIsPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        });
    }

    @MainThread
    @Override
    public void setValue(T value) {
        mIsPending.set(true);
        super.setValue(value);
    }
}
