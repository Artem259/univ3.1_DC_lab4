public class MytReadWriteLock {
    private int readingLock;
    private int writingLock;

    MytReadWriteLock() {
        readingLock = 0;
        writingLock = 0;
    }

    public synchronized void lockReading() {
        if(writingLock == 0) {
            readingLock++;
        }
        else {
            try {
                while (writingLock != 0) {
                    wait();
                }
                readingLock++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void unlockReading() {
        if(readingLock == 0)
            throw new IllegalMonitorStateException();
        readingLock--;
        notify();
    }

    public synchronized void lockWriting() {
        if(writingLock==0 && readingLock==0) {
            writingLock++;
        }
        else {
            try {
                while (writingLock!=0 || readingLock!=0) {
                    wait();
                }
                writingLock++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void unlockWriting() {
        if(writingLock == 0)
            throw new IllegalMonitorStateException();
        writingLock--;
        notify();
    }
}
