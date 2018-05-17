package sample;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MNK {
    private List<Double> xList=new ArrayList<>(Arrays.asList(1.0,2.0,3.0,4.0,5.0));
    private List<Double> yList=new ArrayList<>(Arrays.asList(5.3,6.3,4.8,3.8,3.3));
    private static MNK state;
    private Double xCubeSum;
    private Double xSum;
    private Double xySum;
    private Double ySum;
    private int size;
    private Double det;
    private Double a,b;

    public Double getA() {
        return a/det;
    }
    public String showLists(){
        String s="Массив Х: "+new String();
        for (Double d:xList
             ) {
            s+=d.toString()+"; ";
        }
        s+=" \nМассив Y: ";
        for (Double d:yList
             ) {
            s+=d.toString()+"; ";
        }

        return s;
    }
    public List<Double> getxList() {
        return xList;
    }

    public List<Double> getyList() {
        return yList;
    }

    public Double getB() {
        return b/det;
    }
    public void clearLists(){
        xList.clear();
        yList.clear();
    }
    public void setxList(Double num) {
        this.xList.add(num) ;
    }

    public void setyList(Double num) {
        this.yList.add(num);
    }

    public static MNK getInstance(){
        if(state==null){
            state=new MNK();
        }
        return state;
    }
    public Double getSum(List<Double> arr){
        Double sum=0.;
        for (Double d: arr
                ) {
            sum+=d;
        }
        return sum;
    }
    private List<Double> calcXCube(){
        List<Double> xCube=new ArrayList<>();
        for (Double d:xList
             ) {
            xCube.add(d*d);

        }
        return xCube;
    }
    private List<Double> calcXY(){
        List<Double> xy=new ArrayList<>();
        for (int x=0;x<xList.size();x++)
        {
            xy.add(xList.get(x)*yList.get(x));
        }
           return xy;


    }

    public String showFunc(){
       xCubeSum=getSum(calcXCube());
       xSum=getSum(xList);
       xySum=getSum(calcXY());
       ySum=getSum(yList);
       size=xList.size();
       return xCubeSum+"*a+"+xSum+"*b="+xySum+"\n"
                 +xSum+"*a+"+size+"*b="+ySum+"\n\n";
    }


    public String vector(){
        det=(xCubeSum*size)-(xSum*xSum);
        // det= |a1 a2|
        //      |b1 b1|
        if(det!=0){
            // a= |s1 b1| b=|a1 s1|
            //    |s2 b2|   |a2 s2|
            a=(xySum*size)-(xSum*ySum);
            b=(xCubeSum*ySum)-(xSum*xySum);
            return "y="+a/det+"x+"+b/det+"\n";
        }
        return null;
    }
    public Double cubeDifference(){
        Double a=getA();
        Double b=getB();
        Double eCube=0.;
        for(int i=0;i<yList.size();i++){
            eCube+=Math.pow(yList.get(i)-(a*xList.get(i) + b),2);
            System.out.println(yList.get(i)+" - "+a+" * " +xList.get(i)+" + "+b);
        }
        return eCube;
    }
}
