package org.seefly.juc;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 挑战无锁算法
 * 1、高并发下更好的性能
 * 2、天生免疫死锁
 * 3、实现复杂
 * @author liujianxin
 * @date 2018-11-20 10:36
 */
public class VectorDemo<E> {
    /**
     * 数组+链表？
     */
    private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;
    /**
     *
     */
    private final AtomicReference<Descriptor<E>> descriptor;
    public VectorDemo() {
        buckets = new AtomicReferenceArray<>(30);
        buckets.set(0,new AtomicReferenceArray<>(8));
        descriptor = new AtomicReference<>(new Descriptor<>(0, null));
    }

    public void push_back(E e){
        Descriptor<E> desc;
        Descriptor<E> newd;

        do{
            desc = descriptor.get();
            desc.completeWrite();

            int pos = desc.size + 8;
            int zeroNumPos = Integer.numberOfLeadingZeros(pos);
            //buggggggggggggggggggggg
        }while (true);
    }












    static class Descriptor<E>{
        public int size;
        volatile WriteDescriptor<E> writeop;

        public Descriptor(int size, WriteDescriptor<E> writeop) {
            this.size = size;
            this.writeop = writeop;
        }

        public void completeWrite(){
            WriteDescriptor<E> tmpOp = writeop;
            if(tmpOp != null){
                tmpOp.doIt();
                writeop = null;
            }
        }
    }

    static class WriteDescriptor<E>{
        /**预期值*/
        public E oldV;
        /**新值*/
        public E newV;
        /**bucket*/
        public AtomicReferenceArray<E> addr;
        /**一维索引位置*/
        public int addr_ind;

        /**
         *
         * @param addr bucket
         * @param addr_ind bucket index
         * @param oldV expect value
         * @param newV new value
         */
        public WriteDescriptor( AtomicReferenceArray<E> addr, int addr_ind,E oldV, E newV) {
            this.oldV = oldV;
            this.newV = newV;
            this.addr = addr;
            this.addr_ind = addr_ind;
        }
        public void doIt(){
            addr.compareAndSet(addr_ind,oldV,newV);
        }
    }
}
