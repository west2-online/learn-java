public class Booth {
    private long id;
    private String name;
    private int total;
    private boolean isClosed;
	
	
public Booth(long id, String name, int total, boolean isClosed) {
    this.id = id;
    this.name = name;
    this.total = total;
    this.isClosed = isClosed;
}

public long getId() {
    return id;
}

public void setId(long id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public int getTotal() {
    return total;
}

public void setTotal(int total) {
    this.total = total;
}

public boolean isClosed() {
    return isClosed;
}

public void setClosed(boolean closed) {
    isClosed = closed;
}

@Override
public String toString() {
    return "摊号: " + id + "\n"
	+ "摊主姓名: " 
	+ name + "\n"
    + "在售西瓜数: " + total + "\n"
    + "是否休摊整改: " 
	+ isClosed;
}

public static void purchase(Booth booth, int purchaseQuantity) {
    if (purchaseQuantity > 0 && !booth.isClosed && purchaseQuantity <= booth.total) {
        booth.setTotal(booth.getTotal() - purchaseQuantity);
        System.out.println("购买成功啦，购买了" + purchaseQuantity + "个西瓜。");
    }
    else {
        System.out.println("购买失败咯");
    }
}

public void restock(int restockQuantity) {
    if (restockQuantity > 0 && restockQuantity <= 200) {
        total += restockQuantity;
        System.out.println("进货成功！，进货了" + restockQuantity + "个西瓜。");
    }
    else {
        System.out.println("进货失败咯");
    }
}

public static void closeBooths(Booth[] booths) {
    for (Booth booth : booths) {
        if (!booth.isClosed()) {
            booth.setClosed(true);
            System.out.println(booth.toString());
        }
    }
}