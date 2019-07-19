package com.bryan.newtechtry.rxjava;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bryan.newtechtry.R;
import com.bryan.newtechtry.utils.KLog;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class RxJavaActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);

    }

    private String token;


    /**
     *  retrywhen错误处理
     *  出错则一直执行retrywhen,直到正常为止
     *  返回 error则不执行了
     */



    public void rxjava(View v) {
        disposable = Observable.just(true)
                .flatMap(new Function<Object, ObservableSource<String>>() {

                    @Override
                    public ObservableSource<String> apply(Object o) throws Exception {
                        return t1();
                    }
                })

                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof IllegalArgumentException)
                                    return t2();
                                else
                                    return Observable.error(throwable);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        KLog.e("onNext:" + s + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        KLog.e("onError:" + Thread.currentThread().getName());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        KLog.e("onCompleted:" + Thread.currentThread().getName());
                    }
                });


    }


    private Observable<String> t1() {
        KLog.e("t1");
        if (TextUtils.isEmpty(token)) {
            throw new IllegalArgumentException("token");
        } else if ("error".equals(token)) {
            throw new NullPointerException("error");
        }
        return Observable.just("s1", "s2", "s3");


    }

    private Observable<?> t2() {

        Observable.just(1, 2, 3, 4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        KLog.e("t2 onNext:" + integer + "--" + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        KLog.e("t2 onError" + Thread.currentThread().getName());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        token = "quvck8956";
                        KLog.e("t2 onCompleted" + Thread.currentThread().getName());
                    }
                });

        return Observable.just(true);


    }


    public void printStr() {
        String[] names = {"a", "b", "c"};
        Observable.fromArray(names)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, Thread.currentThread().getName() + s);
                    }
                });

    }


    public void schedulerTest() {
        Observable.just(1, 2, 3, 4, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, Thread.currentThread().getName());
                        Log.e(TAG, "onNext:" + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "onCompleted");
                    }
                });

    }

    /**
     * map 1对1
     * flatMap 1对多
     */

    public void mapTest() {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("qq.jpg");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, String>() {

                    @Override
                    public String apply(String s) throws Exception {
                        Log.e(TAG, "map:");
                        return s.substring(s.lastIndexOf("."));
                    }
                })

                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "onNext:" + s);
                    }
                });
    }


    public void network() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //网络请求
            }


        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "onNext:" + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "onCompleted");
                    }
                });
    }

    String memoryCache = "memory";

    /**
     * 取数据先检查缓存的场景
     * <p>
     * 取数据，首先检查内存是否有缓存
     * 然后检查文件缓存中是否有
     * 最后才从网络中取
     * 前面任何一个条件满足，就不会执行后面的
     */
    public void memoryTest() {
        final Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (memoryCache != null) {
                    e.onNext(memoryCache);
                } else {
                    e.onComplete();
                }
            }

        });
        Observable<String> disk = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String cachePref = "disk";
                if (!TextUtils.isEmpty(cachePref)) {
                    e.onNext(cachePref);
                } else {
                    e.onComplete();
                }
            }

        });

        Observable<String> network = Observable.just("network");

//主要就是靠concat operator来实现
        Observable.concat(memory, disk, network)
                .first("memory")
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "onNext: " + s);
                    }
                });
    }

    /**
     * 合并  有顺序执行
     */
    public void mergeTest() {

        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // int i=1/0;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                e.onNext(555555);
            }


        });
        Observable<Integer> observable2 = Observable.just(4, 5, 6);
        Observable<Integer> observable3 = Observable.just(7, 8, 9);

        Observable.merge(observable1, observable2, observable3)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer != 8;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "onNext: " + integer);
                    }
                } );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable!=null && !disposable.isDisposed()){
            KLog.e("dispose");
            disposable.dispose();
        }
    }


}
