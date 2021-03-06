package com.i502tech.gitclub.api.http.api.subscriber;

import android.util.Log;

import com.i502tech.gitclub.api.http.ResultException;
import com.i502tech.gitclub.api.http.api.BaseResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 实体类的请求
 *
 * @param <T>
 */
public class BeanSubscriber<T> implements Observer<BaseResponse<T>> {

    private static final String TAG = "BaseSubscriber";

    private SubscriberListener<BaseResponse<T>> mListener;
    //用于取消连接
    private Disposable mDisposeable;

    public BeanSubscriber(SubscriberListener<BaseResponse<T>> listener) {
        this.mListener = listener;
    }

    @Override
    public void onError(Throwable e) {
        String msg = "";
        if (e instanceof SocketTimeoutException) {
            msg = "请检查您的网络设置";
        } else if (e instanceof ConnectException|| e instanceof HttpException) {
            msg = "请检查您的网络设置";
        } else if (e instanceof ResultException) {
            msg = ((ResultException) e).getMsg();
        }
        mListener.onFailer(msg);
    }

    @Override
    public void onComplete() {
        Log.i(TAG, "onCompleted: 获取数据完成！");
    }

    @Override
    public void onSubscribe(Disposable d) {
        //拿到Disposable对象可随时取消请求
        mDisposeable = d;
    }

    @Override
    public void onNext(BaseResponse<T> response) {
        if (mListener != null) {
            if (response.isSuccess()) {
                mListener.onSuccess(response);
            } else if (response.isTokenFail()) {
                mListener.onFailer(response.getMsg());
                mListener.onTokenError();
            } else {
                mListener.onFailer(response.getMsg());
            }
        }
    }

}
