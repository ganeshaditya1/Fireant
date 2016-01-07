import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by aditya on 1/5/16.
 *
 * This class helps cache resources that are frequently requested.
 *
 */
public class Bufferpool {

    private int maxSize, currentSize;
    private String baseURI;

    private class page{
        private byte[] data;
        private long dateModified;
        private page next;
        private String location;

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

        String getLocation(){return this.location;}

        private void readFile(String location) throws IOException{
            File tempFile = new File(location);
            FileInputStream temp = new FileInputStream(tempFile);
            this.data = new byte[(int)tempFile.length()];
            temp.read(this.data);
            this.location =location;
            this.dateModified = tempFile.lastModified();
        }
        //Takes the location of a file and loads its data into this object.
        page(String location) throws IOException{
            readFile(location);
            next = null;
        }

        //This loads a new file into a given slot while preserving its link structure.
        public void loadNewFile(String filename) throws IOException{
            readFile(filename);
        }
    }

    private page head, tail;

    public Bufferpool(int noPages, String baseURI){
        this.maxSize = noPages;
        head = tail = null;
        this.currentSize = 0;
        this.baseURI = baseURI;
    }


    //This implements the Least recently used buffer replacement policy.
    public byte[] getResource(String fileName) throws IOException{
        fileName = fileName.equals("/") ? "/index.html" : fileName;
        fileName = baseURI + fileName;

        page poolPage = head, poolPageParent = head;
        synchronized (this){
            //This is executed if the pool is not empty.
            if(poolPage != null) {
                // Search in the pool to see if we can find the file we are looking for.
                for (; poolPage != null; poolPageParent = poolPage, poolPage = poolPage.getNext()) {
                    if(poolPage.getLocation().equals(fileName)){
                        break;
                    }
                }
                //Page was found in the pool itself.
                if(poolPage != null){

                    File temp = new File(fileName);
                    if(temp.lastModified() > poolPage.getDateModified()){
                        System.out.println("AWESOME!!");
                        poolPage.loadNewFile(fileName);
                    }
                    if(poolPageParent != poolPage){
                        poolPageParent.setNext(poolPage.getNext());
                        poolPage.setNext(head);
                        head = poolPage;
                    }
                    return poolPage.getData();
                }
                //Page was not found in the pool.
                else{
                    // check if pool is full.
                    if(currentSize + 1 < maxSize){
                        //pool is not full.
                        //Create a new page and move it to the front of linked list.
                        page temp = new page(fileName);
                        temp.setNext(head);
                        head = temp;
                        currentSize++;
                        return head.getData();
                    }
                    //pool is full
                    else{
                        tail.loadNewFile(fileName);
                        //Find the parent of tail and set it's next to null and move tail to the front of the linked list
                        page parentTail = head;
                        for(; parentTail !=null; parentTail = parentTail.getNext()){
                            if(parentTail.getNext() == tail){
                                break;
                            }
                        }
                        parentTail.setNext(null);
                        tail.setNext(head);
                        head = tail;
                    }
                }
            }
            //Pool is empty.
            else{

                head = new page(fileName);
                tail = head;
                return head.getData();
            }
        }
        return null;
    }

    public void printFileNames(){
        for(page temp = head; temp != null; temp = temp.getNext()){
            System.out.print(temp.getLocation() + "\t");
        }
        System.out.println("");
    }
}
