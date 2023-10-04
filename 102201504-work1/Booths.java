class Booth{
    private long id;
    private String name;
    private int total;
    private boolean isClosed;
    public Booth(){}
    public Booth(long id,String name,int total, boolean isClosed){
        this.id=id;
        this.name=name;
        this.total=total;
        this.isClosed=isClosed;
    }
    public String toString(){
        return "id=" + id + ",name=" + name + ",total=" + total + ",IsClosed=" + isClosed;
    }
    public long getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public int getTotal(){
        return this.total;
    }
    public boolean getIsClosed(){
        return this.isClosed;
    }
    public void setId(long id){
        this.id=id;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setTotal(int total){
        this.total=total;
    }
    public void setIsClosed(boolean isClosed){
        this.isClosed=isClosed;
    }
    public void restock(int n){
        if(n>200||n<=0){
            System.out.println("进货失败");
        }else{
            total+=200;
        }
    }
    public static void purchase(Booth a,int n){
        if(n<=0||a.isClosed||n>a.total){
            System.out.println("购买失败");
        }else{
            System.out.println("购买成功");
            a.total-=n;
        }
    }
    public static void closeBooths(Booth[] booths){
       int n=booths.length;
       for(int i=0;i<n;i++){
           if(!booths[i].isClosed){
               booths[i].isClosed=true;
           }
           booths[i].toString();
        }
    }

}