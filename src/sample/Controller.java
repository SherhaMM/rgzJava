package sample;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;

import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


public class Controller implements Initializable{

    @FXML
    private  TextArea resultTextField;
    @FXML
    private TextField textYField,textXField;
    @FXML
    private LineChart<Number,Number> graphFunc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewResult();
    }

    //Вывод пояснительного текста в текстовое поле
    @FXML
public void viewResult(){
    resultTextField.appendText("----------------------------------------------------------\n");
    resultTextField.appendText("Используем выбранный метод на значениях-\n");
    resultTextField.appendText(MNK.getInstance().showLists()+"\n");
    resultTextField.appendText("Из полученных данных составлена стандартная система: \n");
    resultTextField.appendText(MNK.getInstance().showFunc());
    resultTextField.appendText("Составлена аппроксимирующая функция:\n");
    resultTextField.appendText(MNK.getInstance().vector());
    resultTextField.appendText("Наименьшая сумма квадратов отклонений между теоретическими \n" +
            "и эмпирическими значениями равна: "+MNK.getInstance().cubeDifference()+"\n");
    createLineChart();

}

    // Выводим сплайн на график
public void createSplineChart(){
    clearLineChart();
   double[] fArr=Spline.getInst().getfArr();
    double x[]={0,20,45,53,57,62,74,89,95,100};
    double y[]={0,0,-47,335,26,387,104,0,100,0};

    XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
    XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
    series1.setName("Полученная функция");
    series2.setName("Исходные данные");
//        for(double i=xMin;i<xMax;i+=1){
    for (int i=0;i<100;i++){
        series1.getData().add(new XYChart.Data<>(i,fArr[i]));
    }

    for (int i=0;i<10;i++){
        series2.getData().add(new XYChart.Data<>(x[i],y[i]));
    }
    graphFunc.getData().add(series1);
    graphFunc.getData().add(series2);

}

    // Вывод метода наименьших квадратов на график
    @FXML
public  void createLineChart(){
        clearLineChart();
        Double funcA=MNK.getInstance().getA();
        Double funcB=MNK.getInstance().getB();
        List<Double> xList=MNK.getInstance().getxList();
        List<Double> yList=MNK.getInstance().getyList();
        Double min=xList.get(0);
        Double max=xList.get(xList.size()-1);



        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series1.setName("Полученная функция");
        series2.setName("Исходные данные");
              for(double i= min-5; i<max+5;i++){
//        for (int i=0;i<8;i++){
            Double n=funcA*i+funcB;
            series1.getData().add(new XYChart.Data<>(i,n));
        }
        for(int i=0;i<xList.size();i++){
            series2.getData().add(new XYChart.Data<>(xList.get(i),yList.get(i)));
        }
        graphFunc.getData().add(series1);
        graphFunc.getData().add(series2);
}
public void clearLineChart(){
    graphFunc.getData().clear();
}
public void clearArrays(){
    MNK.getInstance().clearLists();
}
public void clearResults(){
    resultTextField.clear();
}
    //Использование кастомных массивов для мнк
@FXML
   private void getArrays(){
    MNK.getInstance().clearLists();
    clearLineChart();
    clearResults();
    resultTextField.appendText("ВВОДИТЕ РАВНЫЕ ЗНАЧЕНИЯ X Y!!!1\n");
    textYField.setEditable(true);
    textXField.setEditable(true);
       Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

       UnaryOperator<TextFormatter.Change> filter = c -> {
           String text = c.getControlNewText();
           if (validEditingState.matcher(text).matches()) {
               return c ;
           } else {
               return null ;
           }
       };

       StringConverter<Double> converter = new StringConverter<Double>() {

           @Override
           public Double fromString(String s) {
               if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                   return 0.0 ;
               } else {
                   return Double.valueOf(s);
               }
           }
           @Override
           public String toString(Double d) {
               return d.toString();
           }
       };
        TextFormatter<Double> textFormatterX = new TextFormatter<>(converter, 0.0, filter);
        TextFormatter<Double> textFormatterY = new TextFormatter<>(converter, 0.0, filter);
       textXField.setTextFormatter(textFormatterX);
       textYField.setTextFormatter(textFormatterY);

       textFormatterX.valueProperty().addListener((ObservableValue<? extends Double> obs, Double oldValue, Double newValue) -> {
           resultTextField.appendText(newValue.doubleValue()+" -добавлено в массив Х \n");
           MNK.getInstance().setxList(newValue.doubleValue());
       });

         textFormatterY.valueProperty().addListener((ObservableValue<? extends Double> obs, Double oldValue, Double newValue) -> {
        MNK.getInstance().setyList(newValue.doubleValue());
        System.out.println("User entered value: "+newValue.doubleValue());
        resultTextField.appendText(newValue.doubleValue()+" -добавлено в массив Y \n");
    });
   }
   }



