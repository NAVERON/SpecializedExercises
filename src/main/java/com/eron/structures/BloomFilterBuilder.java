package com.eron.structures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * java 实现的 BloomFilter  https://github.com/magnuss/java-bloomfilter
 *
 * @author magnuss
 * google 的guava 有现成的实现 可以直接使用
 */
public class BloomFilterBuilder {

    private static final Logger log = LoggerFactory.getLogger(BloomFilterBuilder.class);

    public static void main(String[] args) {
        BloomFilterBuilder builder = new BloomFilterBuilder();

        builder.guavaBloomFilterUsgae();
    }

    // 使用guava的实现
    public void guavaBloomFilterUsgae() {
        BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), 100000, 0.01);

        filter.put(3);  // 喂数据
        log.info("guava bloom filter -> test : {}, mightContain : {}", filter.test(7), filter.mightContain(3));
    }


    /**
     * Implementation of a Bloom-filter, as described here:
     * http://en.wikipedia.org/wiki/Bloom_filter
     *
     * For updates and bugfixes, see http://github.com/magnuss/java-bloomfilter
     *
     * Inspired by the SimpleBloomFilter-class written by Ian Clarke. This
     * implementation provides a more evenly distributed Hash-function by
     * using a proper digest instead of the Java RNG. Many of the changes
     * were proposed in comments in his blog:
     * http://blog.locut.us/2008/01/12/a-decent-stand-alone-java-bloom-filter-implementation/
     *
     * @param <E> Object type that is to be inserted into the Bloom filter, e.g. String or Integer.
     * @author Magnus Skjegstad <magnus@skjegstad.com>
     */
    // 暂时不写实现 冗余, 访问链接直接查看源代码 : https://github.com/magnuss/java-bloomfilter


}










