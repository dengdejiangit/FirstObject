package com.example.administrator.testproject.code.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;

import com.example.administrator.testproject.R;
import com.example.administrator.testproject.base.BaseActivity;
import com.example.administrator.testproject.http.BaseObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author WS
 * @date 2019-8-1
 */

public class RxJavaTestActivity extends BaseActivity {

    private int i = 1;

    public static void start(Context context) {
        Intent starter = new Intent(context, RxJavaTestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_test);
        /*
         * 事件创建的操作符
         * 即时创建：just、fromArray、formIterable、
         * 延时创建：defer、timer、interval、intervalRange、range、rangeLong
         * */
        testOperato();
        /*
         * 事件转换操作符
         * map、flatMap、concatMap、buffer
         * */
        textChangeOperato();

        //组合多个被观察者一起发送数据，合并后 按发送顺序串行执行
        concatConcatArray();
        //类似于concat，同样是组合多个被观察者一起发送数据，但concat（）操作符合并后是按发送顺序串行执行
        mergeMergeArray();
        //zip变换操作符进行事件合并
        zip();
        //count统计发送事件的个数
        count();
        //在一个被观察者发送事件前，追加发送一些数据 / 一个新的被观察者
        startWithStartWithArray();
        //将被观察者Observable发送的数据事件收集到一个数据结构里
        collect();
        //把被观察者需要发送的事件聚合成1个事件 &
        reduce();
        //当两个Observables中的任何一个发送了数据后，
        // 将先发送了数据的Observables 的最新（最后）一个数据 与
        // 另外一个Observable发送的每个数据结合，最终基于该函数的结果发送数据
        //与Zip（）的区别：Zip（） = 按个数合并，即1对1合并；CombineLatest（） = 按时间合并，即在同一个时间点上合并
        combineLatest();


    }

    private void combineLatest() {
        Observable.combineLatest(
                // 第1个发送数据事件的Observable
                Observable.just(1L, 2L, 3L),
                // 第2个发送数据事件的Observable：从0开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS),
                new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long o1, Long o2) throws Exception {
                        // o1 = 第1个Observable发送的最新（最后）1个数据
                        // o2 = 第2个Observable发送的每1个数据
                        return o1 + o2;
                        // 合并的逻辑 = 相加
                        // 即第1个Observable发送的最后1个数据 与 第2个Observable发送的每1个数据进行相加
                    }
                })
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    public void next(Long aLong) {
                        //3(3+0),4(3+1),5(3+2)
                    }
                });
    }

    private void reduce() {
        Observable.just(1, 2, 3, 4)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    // 在该复写方法中复写聚合的逻辑
                    @Override
                    public Integer apply(@NonNull Integer s1, @NonNull Integer s2) throws Exception {
                        return s1 * s2;
                        // 本次聚合的逻辑是：全部数据相乘起来
                        // 原理：第1次取前2个数据相乘，之后每次获取到的数据 = 返回的数据x原始下1个数据每
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer s) throws Exception {
                        Log.e(TAG, "最终计算的结果是： " + s);
                        //  24 = 1*2*3*4
                    }
                });
    }

    private void collect() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .collect(
                        // 1. 创建数据结构（容器），用于收集被观察者发送的数据
                        new Callable<ArrayList<Integer>>() {
                            @Override
                            public ArrayList<Integer> call() throws Exception {
                                return new ArrayList<>();
                            }
                            // 2. 对发送的数据进行收集
                        }, new BiConsumer<ArrayList<Integer>, Integer>() {
                            @Override
                            public void accept(ArrayList<Integer> list, Integer integer)
                                    throws Exception {
                                // 参数说明：list = 容器，integer = 后者数据
                                list.add(integer);
                                // 对发送的数据进行收集
                            }
                        })
                .subscribe(new Consumer<ArrayList<Integer>>() {
                    @Override
                    public void accept(ArrayList<Integer> integers) throws Exception {
                        Log.e("WS==--", "" + integers);
                        //[1,2,3,4,5,6]
                    }
                });
    }

    private void startWithStartWithArray() {
        // 注：追加数据顺序 = 后调用先追加
        Observable.just(4, 5, 6)
                // 追加单个数据 = startWith()
                .startWith(0)
                // 追加多个数据 = startWithArray()
                .startWithArray(1, 2, 3)
                .subscribe(new BaseObserver<Integer>() {
                    @Override
                    public void next(Integer integer) {
                        Log.e("WS==--", "" + integer);
                        //1,2,3,0,4,5,6
                    }
                });


        //<-- 在一个被观察者发送事件前，追加发送被观察者 & 发送数据 -->
        // 注：追加数据顺序 = 后调用先追加
        Observable.just(4, 5, 6)
                .startWith(Observable.just(1, 2, 3))
                .subscribe(new BaseObserver<Integer>() {
                    @Override
                    public void next(Integer integer) {
                        Log.e("WS==--", "" + integer);
                        //1,2,3,4,5,6
                    }
                });

    }

    private void count() {
        Observable.just(1, 2, 3, 4)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e("WS==--", "事件数量：" + aLong);
                        //4
                    }
                });
    }

    private void mergeMergeArray() {
        //组合操作符  merge  和   mergeArray
        //组合多个被观察者一起发送数据，合并后 按时间线并行执行
        //二者区别：组合被观察者的数量，即merge（）组合被观察者数量≤4个，而mergeArray（）则可＞4个
        //区别上述concat（）操作符：同样是组合多个被观察者一起发送数据，但concat（）操作符合并后是按发送顺序串行执行
        Observable.merge(
                // 从0开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS),
                // 从2开始发送、共发送3个数据、第1次事件延迟发送时间 = 1s、间隔时间 = 1s
                Observable.intervalRange(2, 3, 1, 1, TimeUnit.SECONDS))
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    public void next(Long value) {
                        Log.e("WS==--", "merge操作符合并后数据：" + value);
                    }
                });
    }

    private void zip() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "被观察者1发送了事件1");
                emitter.onNext(1);
                // 为了方便展示效果，所以在发送事件后加入2s的延迟
                Thread.sleep(1000);

                Log.d(TAG, "被观察者1发送了事件2");
                emitter.onNext(2);
                Thread.sleep(1000);

                Log.d(TAG, "被观察者1发送了事件3");
                emitter.onNext(3);
                Thread.sleep(1000);

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        // 设置被观察者1在工作线程1中工作

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "被观察者2发送了事件A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件C");
                emitter.onNext("C");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件D");
                emitter.onNext("D");
                Thread.sleep(1000);

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());
        // 设置被观察者2在工作线程2中工作
        // 假设不作线程控制，则该两个被观察者会在同一个线程中工作，即发送事件存在先后顺序，而不是同时发送

        //<-- 使用zip变换操作符进行事件合并 -->
        // 注：创建BiFunction对象传入的第3个参数 = 合并后数据的数据类型
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String string) throws Exception {
                return integer + string;
            }
        }).subscribe(new BaseObserver<String>() {
            @Override
            public void next(String s) {
                Log.e("WS==--", "zip进行合并后的的字符：" + s);
            }
        });
    }

    private void concatConcatArray() {
        //组合操作符 concat 与 concatArray
        //组合多个被观察者一起发送数据，合并后 按发送顺序串行执行
        //二者区别：组合被观察者的数量，即concat（）组合被观察者数量≤4个，而concatArray（）则可＞4个
        Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5), Observable.just(6))
                .subscribe(new BaseObserver<Integer>() {
                    @Override
                    public void next(Integer integer) {
                        Log.e("WS==--", "concat操作符合并后：" + integer);
                    }
                });

    }

    private void textChangeOperato() {
        //对 被观察者发送的每1个事件都通过 指定的函数 处理，从而变换成另外一种事件
        //可以对 实体类进行修改
        Observable.just("1", "2", "3").map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                //转换操作
                return Integer.valueOf(s);
            }
        }).subscribe(new BaseObserver<Integer>() {
            @Override
            public void next(Integer integer) {
                Log.e("WS==--", "经过map操作符转换后的数据：" + integer);
            }
        });

        //为事件序列中每个事件都创建一个 Observable 对象；
        //将对每个 原始事件 转换后的 新事件 都放入到对应 Observable对象；
        //将新建的每个Observable 都合并到一个 新建的、总的Observable 对象；
        //新建的、总的Observable 对象 将 新合并的事件序列 发送给观察者（Observer）
        Observable.just(1, 2, 3, 4).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {

                List<String> list = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    list.add(String.valueOf(integer));
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new BaseObserver<String>() {
            @Override
            public void next(String s) {
                Log.e("WS==--", "flatMap转换操作符,转换后的数据：" + s);
            }
        });

        //类似于flatMap操作符，不同点在于将会有序的将被观察者发送的整个事件序列进行变换  ==》 1,2,3,4,1,2,3,4,。。。
        Observable.just(1, 2, 3, 4).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {

                List<String> list = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    list.add(String.valueOf(integer));
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new BaseObserver<String>() {
            @Override
            public void next(String s) {
                Log.e("WS==--", "flatMap转换操作符,转换后的数据：" + s);
            }
        });

        //定期从 被观察者（Obervable）需要发送的事件中 获取一定数量的事件 & 放到缓存区中，最终发送
        //1，2,3  ，2,3,4  ，3,4,5  ，4，5  ，5     buffer后参数一：周期大小   参数二：每次周期后增加数量
        Observable.just(1, 2, 3, 4, 5)
                // 设置缓存区大小 & 步长
                .buffer(3, 1)
                // 缓存区大小 = 每次从被观察者中获取的事件数量
                // 步长 = 每次获取新事件的数量
                .subscribe(new BaseObserver<List<Integer>>() {
                    @Override
                    public void next(List<Integer> integerList) {

                    }
                });

    }

    private void testOperato() {
        //直接发送 传入的事件 注：最多只能发送10个参数
        Observable.just(1, 2, 3).subscribe(new BaseObserver<Integer>() {
            @Override
            public void next(Integer integer) {
                Log.e("WS==--", "just操作符对应数字:" + integer);
            }
        });

        String[] strings = {"一", "二", "三", "四"};
        //直接发送 传入的数组数据
        Observable.fromArray(strings)
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void next(String s) {
                        Log.e("WS==--", "fromArray操作符对应字符：" + s);
                    }
                });

        List<String> list = new ArrayList<>();
        list.add("一");
        list.add("二");
        list.add("三");
        list.add("四");
        list.add("五");
        //直接发送 传入的集合List数据
        Observable.fromIterable(list).subscribe(new BaseObserver<String>() {
            @Override
            public void next(String s) {
                Log.e("WS==--", "formIterable操作符对应字符:" + s);
            }
        });


        //直到有观察者（Observer ）订阅时，才动态创建被观察者对象（Observable） & 发送事件
        Observable<Integer> deferObservable = Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                return Observable.just(i);
            }
        });
        i = 2;
        deferObservable.subscribe(new BaseObserver<Integer>() {
            @Override
            public void next(Integer integer) {
                Log.e("WS==--", "defer操作符对应数字：" + integer);
            }
        });

        Log.e("timer", "timer操作符对应的现在时间：" + Calendar.getInstance().get(Calendar.SECOND));
        //延迟指定时间后，发送1个数值0（Long类型）,延时执行任务
        Observable.timer(1, TimeUnit.SECONDS).subscribe(new BaseObserver<Long>() {
            @Override
            public void next(Long aLong) {
                Log.e("timer", "timer操作符对应的现在时间：" + Calendar.getInstance().get(Calendar.SECOND));
            }
        });

        Log.e("interval", "interval操作符对应的现在时间：" + Calendar.getInstance().get(Calendar.SECOND));
        //每隔指定时间 就发送 事件
        //参数一：第一次延时时间   参数二：间隔时间    参数三：时间单位
        //发送的事件序列 = 从0开始、无限递增1的的整数序列
        Observable.interval(2, 1, TimeUnit.SECONDS).subscribe(new BaseObserver<Long>() {
            @Override
            public void next(Long aLong) {
                Log.e("interval", "interval操作符对应的现在时间：" + Calendar.getInstance().get(Calendar.SECOND));
            }
        });

        Log.e("intervalRange", "intervalRange操作符对应的现在时间：" + Calendar.getInstance().get(Calendar.SECOND));
        //每隔指定时间 就发送 事件，可指定发送的数据的数量
        //参数一：起始事件位置   参数二：循环次数   参数三：第一次延时时间   参数四：时间间隔    参数五：时间单位
        //发送的事件序列 = 从0开始、无限递增1的的整数序列   ==> 2,3,4,5,6
        Observable.intervalRange(2, 5, 2, 1, TimeUnit.SECONDS).subscribe(new BaseObserver<Long>() {
            @Override
            public void next(Long aLong) {
                Log.e("intervalRange", "intervalRange操作符对应的现在时间：" + Calendar.getInstance().get(Calendar.SECOND));
                Log.e("intervalRange", "intervalRange操作符对应的现在事件位置：" + aLong);
            }
        });

        //连续发送 1个事件序列，可指定范围
        //发送的事件序列 = 从0开始、无限递增1的的整数序列
        //参数一：起始事件位置   参数二：循环次数
        Observable.range(3, 7).subscribe(new BaseObserver<Integer>() {
            @Override
            public void next(Integer integer) {
                Log.e("range", "range操作符对应的的数字：" + integer);
            }
        });

        //类似于range（），区别在于该方法支持数据类型 = Long
        Observable.rangeLong(3, 7).subscribe(new BaseObserver<Long>() {
            @Override
            public void next(Long aLong) {
                Log.e("rangeLong", "rangeLong操作符对应的的数字：" + aLong);
            }
        });


    }

// 下列方法一般用于测试使用
//<-- empty()  -->
    // 该方法创建的被观察者对象发送事件的特点：仅发送Complete事件，直接通知完成
//    Observable observable1=Observable.empty();
// 即观察者接收后会直接调用onCompleted（）

//<-- error()  -->
    // 该方法创建的被观察者对象发送事件的特点：仅发送Error事件，直接通知异常
// 可自定义异常
//    Observable observable2=Observable.error(new RuntimeException())
// 即观察者接收后会直接调用onError（）

//            <-- never()  -->
// 该方法创建的被观察者对象发送事件的特点：不发送任何事件
//            Observable observable3=Observable.never();
// 即观察者接收后什么都不调用
}
