package cn.myhomespace.zhou.object;

/**
 * describe:
 * 线程资源配置
 * @author zhouwenchao
 * @date 2018/05/04
 */
public class SourceConfig {

    private int threadNum;
    private int queueSize;

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
