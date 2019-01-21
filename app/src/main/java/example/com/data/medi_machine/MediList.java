package example.com.data.medi_machine;

public class MediList{

    String name;
    String addr;
    String tel;


    public void setTel(String tel){
        this.tel = tel;
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

    public String getTel(){
        return tel;
    }

    public String getaddr(){
        return addr;
    }

}

