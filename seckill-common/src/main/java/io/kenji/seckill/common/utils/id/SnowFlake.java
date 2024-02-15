package io.kenji.seckill.common.utils.id;


/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-04
 **/
public class SnowFlake {
    private static final long START_TIME_STAMP = 1707056570674L;

    /**
     * Each part occupy bit
     */

    private final static long SEQUENCE_BIT = 12; // sequence occupy bit
    private final static long MACHINE_BIT = 5; // machine occupy bit
    private final static long DATACENTER_BIT = 5; // data center occupy bit
    /**
     * Max value of each part
     */
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE_NUM = ~(-1L << SEQUENCE_BIT);

    /**
     * Left movement for each part
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + DATACENTER_BIT;
    private final static long TIME_STAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;

    private long machineId;

    private long sequence = 0L;
    private long lastTimeStamp = -1L;


    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenter id can not be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId id can not be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    public synchronized long nextId() {
        long currentTimeStamp = getNewTimeStamp();
        if (currentTimeStamp < lastTimeStamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }
        if (currentTimeStamp == lastTimeStamp) {
            // Sequence increase in same millisecond
            sequence = (sequence + 1) & MAX_SEQUENCE_NUM;
            if (sequence == 0L) {
                currentTimeStamp = getNextMill();
            }

        } else {
            // Set sequence as 0 in the different millisecond
            sequence = 0L;
        }
        lastTimeStamp = currentTimeStamp;

        return (currentTimeStamp - START_TIME_STAMP) << TIME_STAMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getNewTimeStamp();
        while (mill <= lastTimeStamp) {
            mill = getNewTimeStamp();
        }
        return mill;
    }

    private long getNewTimeStamp() {
        return System.currentTimeMillis();
    }

}
