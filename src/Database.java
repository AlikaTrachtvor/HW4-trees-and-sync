import java.util.HashMap;
import java.util.Map;

public class Database {
    /***
     * This class represents a database and allows threads to write and read from the database, while maintaining
     * synchronization between the different threads in order to prevent clashing and overload
     */
    private Map<String, String> data;
    private int maxNumOfReaders;
    private int currNumOfReaders;
    private Map<String, Thread> currReaders;
    private Thread currWriter;
    private boolean isAnyWriting;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();// Note: You may add fields to the class and initialize them in here. Do not add parameters!
        this.maxNumOfReaders = maxNumOfReaders;
        this.currNumOfReaders = 0;
        this.currReaders = new HashMap<>();
        this.currWriter = null;
        this.isAnyWriting = false;
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    /***
     * This method checks whether a thread is allowed to read from the database
     * @return true if it's allowed to read and false otherwise
     */
    public synchronized boolean readTryAcquire() {
        boolean allowedRead = false;
        Thread currThread = Thread.currentThread();
        if(this.currNumOfReaders < this.maxNumOfReaders && this.isAnyWriting == false){
            this.currReaders.put(currThread.getName(), currThread);
            this.currNumOfReaders++;
            allowedRead = true;
        }
        return allowedRead;
    }

    /***
     * This method checks whether a thread is allowed to read from the database. If the thread is not allowed,
     * it is forced to wait.
     */
    public synchronized void readAcquire()  {
        try {
            while (!readTryAcquire())
                wait();
        }
        catch (InterruptedException | IllegalMonitorStateException e){}
    }

    /***
     * This method releases a thread that is done reading from the database and notifies the rest of the threads
     * @throws IllegalMonitorStateException if a thread that is not reading from the database tries to release
     * space for other threads
     */
    public synchronized void readRelease() {
        Thread currThread = Thread.currentThread();
        if(currReaders.containsKey(currThread.getName())) {
            this.currReaders.remove(currThread.getName());
            this.currNumOfReaders--;
            notifyAll();
        }
        else
            throw new IllegalMonitorStateException("Illegal read release attempt");
    }

    /***
     * This method checks whether a thread is allowed to write to the database. If the thread is not allowed,
     * it is forced to wait.
     */
    public synchronized void writeAcquire() {
        try{
            while(!writeTryAcquire())
                wait();
        }
        catch (InterruptedException | IllegalMonitorStateException e){}
    }

    /***
     * This method checks whether a thread is allowed to write to the database
     * @return true if it's allowed to read and false otherwise
     */
    public synchronized boolean writeTryAcquire() {
        boolean allowedWrite = false;
        if(this.isAnyWriting == false && this.currNumOfReaders == 0){
            this.currWriter = Thread.currentThread();
            this.isAnyWriting = true;
            allowedWrite = true;
        }
        return allowedWrite;
    }

    /***
     * This method releases a thread that is done writing to the database and notifies the rest of the threads
     * @throws IllegalMonitorStateException if a thread that is not writing to the database tries to release
     * space for other threads
     */
    public synchronized void writeRelease() {
        if(this.currWriter != null && this.currWriter.equals(Thread.currentThread())) {
            this.currWriter = null;
            this.isAnyWriting = false;
            notify();
        }
        else
            throw new IllegalMonitorStateException("Illegal write release attempt");
    }
}
