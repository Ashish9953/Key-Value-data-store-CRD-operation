package database1.database1;
class MyThread1 extends Thread{
	databaseTTL obj=new databaseTTL("settings.properties",false,2L);
	private databaseTTL t;
    MyThread1(databaseTTL t){
        this.t=t;
    }
    public void run(){
        try {
            t.createDatabase("ABC","WXYZ");
            t.createDatabase("ede","ghar");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(t.readDatabase("ede"));
        t.delete("ABC");
    }

}
class MyThread2 extends Thread{
	databaseTTL obj=new databaseTTL("settings.properties",false,2L);
	private databaseTTL t;
    MyThread2(databaseTTL t){
        this.t=t;
    }
    public void run(){
        try {
            t.createDatabase("FirstName","Ashish");
            t.createDatabase("LastName","kumar");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(t.readDatabase("FirstName"));
        t.delete("LastName");
        t.getAll();
    }


}
public class databaseExample {
	 public static void main(String[] args) throws Exception {
			// write your code here
		        databaseTTL obj=new databaseTTL("settings.properties",false,6000000L);
		        obj.createDatabase("NickName","Amar");
		        obj.createDatabase("Hobby","cricket");
		        System.out.println(obj.readDatabase("Hobby"));
		        obj.getAll();
		        MyThread1 t1=new MyThread1(obj);
		        t1.start();
		        MyThread2 t2=new MyThread2(obj);
		        t2.start();
		    }
		}


