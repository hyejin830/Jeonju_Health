package example.com.listframe;

public class BasicList{

    String name;
    String addr;
    String datanum;


    public void setNum(String datanum){
        this.datanum = datanum;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAddr(String addr){
        this.addr = addr;
    }

    public String getName(){
        return name;
    }

    public String getNum(){
        return datanum;
    }

    public String getaddr(){
        return addr;
    }

}
