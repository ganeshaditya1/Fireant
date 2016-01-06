/**
 * Created by aditya on 1/5/16.
 *
 * This class helps cache resources that are frequently requested.
 *
 */
public class Bufferpool {
    private int maxSize, currentSize;
    private class page{
        private byte[] data;
        private long dateModified;
        private page next;

        byte[] getData() {
            return data;
        }

        page getNext(){
            return next;
        }

        void setNext(page next){
            this.next = next;
        }

        long getDateModified(){
            return dateModified;
        }

        void setDateModified(long dataModified){
            this.dateModified = dataModified;
        }

        //Takes the location of a file and loads its data into this object.
        page(String location){}
    }

    private page head, tail;

    public Bufferpool(int noPages){}

    //This implements the Least recently used buffer replacement policy.
    public byte[] getResource(String fileName){return null;}
}
