//package com.seamlabs.mvpdriver.Common.service;
//
//import io.reactivex.Observable;
//import io.reactivex.subjects.PublishSubject;
//
//public class RxBus {
//
//    private static RxBus mInstance;
//
//    public static RxBus getInstance() {
//        if (mInstance == null) {
//            mInstance = new RxBus();
//        }
//        return mInstance;
//    }
//
//    private RxBus() {
//    }
//
//    private PublishSubject<String> publisher = PublishSubject.create();
//
//    void publish(String event) {
//        publisher.onNext(event);
//    }
//
//    public  Observable<String> listen() {
//        return publisher;
//    }
//}
