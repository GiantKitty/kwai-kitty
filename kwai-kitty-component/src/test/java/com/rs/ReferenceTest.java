package com.rs;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.WeakHashMap;

import org.junit.Test;

import com.rs.model.User;

import sun.misc.Cleaner;


/**
 * 强、软、弱、虚
 *
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-12
 */
public class ReferenceTest {

    public void test() {
        String strongReference = new String("abc");  // 强引用
        String str = new String("abc");    // 强引用
        SoftReference<String> softReference = new SoftReference<>(str);  // 软引用
    }

    /**
     * 软引用可以和一个引用队列（ReferenceQueue）联合使用
     * <p>
     * 垃圾收集线程会在JVM抛出OutOfMemoryError之前回收软引用对象，而且JVM会尽可能优先回收长时间闲置不用的软引用对象。
     * 对那些刚构建的或刚使用过的“较新的”软对象会被虚拟机尽可能保留，这就是引入引用队列ReferenceQueue的原因，
     * 队列是先进先出，通过这个引用队列，可以跟踪对象的回收情况。
     */
    @Test
    public void test_ReferenceQueue() {
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        String str = new String("abc");
        SoftReference<String> softReference = new SoftReference<>(str, referenceQueue);
        str = null;
        System.gc();  // Notify GC
        System.out.println(softReference.get()); // 输出：abc 意味着abc没有被gc掉

        Reference<? extends String> poll = referenceQueue.poll();
        System.out.println(poll); // 输出：null 因为abc没有被gc所以没加到queue里面
    }


    /**
     * 浏览器的缓存页面功能，也可以用软引用
     * <p>
     * 软引用可用来实现内存敏感的高速缓存。一方面内存不足的时候可以回收，一方面也不会频繁回收
     */
    @Test
    public void test_() {
        Browser browser = new Browser();   // 获取浏览器对象进行浏览
        BrowserPage page = browser.getPage();   // 从后台程序加载浏览页面
        // 将浏览完毕的页面置为软引用
        SoftReference softReference = new SoftReference(page);

        if (softReference.get() != null) {  // 回退或者再次浏览此页面时
            // 内存充足，还没有被回收器回收，直接获取缓存
            page = (BrowserPage) softReference.get();
        } else {   // 内存不足，软引用的对象已经回收
            page = browser.getPage();  // 再次从后台程序加载
            softReference = new SoftReference(page);   // 重新构建软引用
        }
    }


    /**
     * 在gc线程一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。
     * 软引用会影响对象的生命周期，弱引用不会。
     */
    public void test_WeakReference() {
        String str = new String("abc");
        WeakReference<String> weakReference = new WeakReference<>(str);
        // 弱引用 → 强引用
        String strongReference = weakReference.get();
    }


    public class GCTarget {
        public String id; // 对象的ID
        byte[] buffer = new byte[1024]; // 占用内存空间

        public GCTarget(String id) {
            this.id = id;
        }

        protected void finalize() throws Throwable {
            System.out.println("Finalizing GCTarget, id is : " + id);
        }
    }

    public class GCTargetWeakReference extends WeakReference<GCTarget> {
        public String id; // 弱引用的ID

        public GCTargetWeakReference(GCTarget gcTarget,
                ReferenceQueue<? super GCTarget> queue) {
            super(gcTarget, queue);
            this.id = gcTarget.id;
        }

        protected void finalize() {
            System.out.println("Finalizing GCTargetWeakReference " + id);
        }
    }

    // 引用队列
    private final static ReferenceQueue<GCTarget> REFERENCE_QUEUE = new ReferenceQueue<>();

    @Test
    public void test_WeakReference2() {
        LinkedList<GCTargetWeakReference> gcTargetList = new LinkedList<>();
        // 创建弱引用的对象，依次加入链表中
        for (int i = 0; i < 5; i++) {
            GCTarget gcTarget = new GCTarget(String.valueOf(i));
            GCTargetWeakReference weakReference = new GCTargetWeakReference(gcTarget, REFERENCE_QUEUE);
            gcTargetList.add(weakReference);

            System.out.println("Just created GCTargetWeakReference obj: " + gcTargetList.getLast());
        }
        System.gc(); // 通知GC进行垃圾回收
        try {
            Thread.sleep(6000);// 休息几分钟，等待上面的垃圾回收线程运行完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 检查关联的引用队列是否为空
        Reference<? extends GCTarget> reference;
        while ((reference = REFERENCE_QUEUE.poll()) != null) {
            if (reference instanceof GCTargetWeakReference) {
                System.out.println("In queue, id is: " + ((GCTargetWeakReference) (reference)).id);
            }
        }
    }

    @Test
    public void test_WeakHashMap() throws InterruptedException {
        WeakHashMap<User, String> weakHashMap = new WeakHashMap();
        //强引用
        User zhangsan = new User("zhangsan", 24);
        weakHashMap.put(zhangsan, "zhangsan");
        System.out.println("有强引用的时候:map大小" + weakHashMap.size());

        zhangsan = null;//去掉强引用
        System.gc();
        Thread.sleep(1000);
        System.out.println("无强引用的时候:map大小"+weakHashMap.size());
    }

    @Test
    public void test_WeakReference3() throws InterruptedException {
        String str = new String("hello"); //--1--
        ReferenceQueue<String> rq = new ReferenceQueue<String>();  //--2--
        WeakReference<String> wf = new WeakReference<String>(str, rq); //--3--
        str = null; //--4--取消"hello"对象的强引用,现在只有一个弱引用在指向hello对象
        System.out.println(wf.get());  //--5--假如"hello"对象没有被回收，str1引用"hello"对象

        //假如"hello"对象没有被回收，rq.poll()返回 null
        Reference<? extends String> ref = rq.poll(); //--6--
        System.out.println(ref);

        //两次催促垃圾回收器工作，提高"hello"对象被回收的可能性
        System.gc();  //7
        System.gc();  //8
        Thread.sleep(1000);
        System.out.println(wf.get()); //7 假如"hello"对象被回收，str1为null
        ref = rq.poll(); //9
        System.out.println(ref);
    }


    /**
     * 虚引用可以用来做为对象是否存活的监控
     * 虚引用必须和引用队列(ReferenceQueue)联合使用。
     *
     * 当垃圾回收器，准备回收一个对象时，如果发现它还有虚引用，就会把这个对象加入到引用队列中，然后再进行gc，在其关联的虚引用出队前，不会彻底销毁该对象。
     * 程序可以通过判断引用队列中是否已经加入了某个虚引用的对象，来判断这个对象是否将要进行垃圾回收。
     *
     * 与软引用和弱引用不同，显式使用虚引用可以阻止对象被清除，
     * 只有在程序中显式或者隐式移除这个虚引用时，这个已经执行过finalize方法的对象才会被清除。
     * 想要显式移除虚引用的话，只需要将其从引用队列中取出然后扔掉（置为null）即可。
     */
    @Test
    public void test_PhantomReference() {
        String str = new String("疯狂Java讲义");  // 创建一个字符串对象
        ReferenceQueue rq = new ReferenceQueue();  // 创建一个引用队列
        PhantomReference pr = new PhantomReference (str , rq);  // 创建一个虚引用，让此虚引用引用到"疯狂Java讲义"字符串
        // 切断str引用和"疯狂Java讲义"字符串之间的引用
        str = null;
        // 取出虚引用所引用的对象，并不能通过虚引用获取被引用的对象，
        System.out.println(pr.get());  // 此处输出null
        // 强制垃圾回收
        System.gc();
        System.runFinalization();
        // 垃圾回收之前，虚引用将被放入引用队列中
        // 取出引用队列中最先进入队列中的引用与pr进行比较
        System.out.println(rq.poll() == pr);   // 输出 true
    }


    /**
     * 对于一个堆外内存来说，由于他在JVM管控之外，所以gc回收器是无法直接gc他的，怎么办呢？java是怎么来回收一个堆外内存的呢？
     * 答案就是【虚引用】，每一个【堆外内存】，都有一个在【堆内的引用】 ，叫做DirectByteBuffer，同时，有一个【虚引用】指向 DirectByteBuffer
     * 当DirectByteBuffer需要被gc时，由于他有【虚引用】，所以DirectByteBuffer会在被gc之前，加入到【引用队列】中，
     * 一旦【引用队列】检测到有对象进来了，说明这个对象马上要被gc了，于是堆外内存就知道自己该被gc了。
     */
    @Test
    public void test_1() throws InterruptedException {
        // Cleaner是PhantomReference的子类
        Cleaner.create(new User("zhangsan", 24), () -> {
            System.out.println("我被回收了，当前线程:"+ Thread.currentThread().getName());
        });
        System.gc();
        Thread.sleep(1000);
    }

    /**
     * 以下是与逻辑无关的实体类  ========
     */
    private class Browser {
        private BrowserPage page;

        public BrowserPage getPage() {
            return this.page;
        }
    }

    private class BrowserPage {}

}
