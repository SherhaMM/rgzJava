package sample;


public class Spline {
    private int n; // Количество узлов сетки
    private spline_tuple[] splines= new spline_tuple[10]; // Сплайн
    double[] fArr=new double[100];

    public Spline() {
        double x[]={0,20,45,53,57,62,74,89,95,100};
        double y[]={0,0,-47,335,26,387,104,0,100,0};
        build_spline(x,y,10);
        for (int i=0;i<100;i++){
            fArr[i]=f(i);
        }
    }
    public static Spline getInst(){
        Spline s= new Spline();
        return s;
    }

    public double[] getfArr() {
        return fArr;
    }

// x - узлы сетки, должны быть упорядочены по возрастанию, кратные узлы запрещены
// y - значения функции в узлах сетки
// n - количество узлов сетки


    public final void build_spline(double[] x, double[] y, int n)
    {
        this.n = n;

//         Инициализация массива сплайнов
        for (int i = 0; i < n; i++)
        {
            splines[i] = new spline_tuple();
            splines[i].x= new spline_tuple().x;
            splines[i].a= new spline_tuple().a;
            splines[i].c= new spline_tuple().x;

        }
        for (int i = 0; i < n; ++i)
        {
            splines[i].x = x[i];
            splines[i].a = y[i];
        }
        splines[0].c = 0.0;

        // Решение СЛАУ относительно коэффициентов сплайнов c[i] методом прогонки для трехдиагональных матриц
        // Вычисление прогоночных коэффициентов - прямой ход метода прогонки
        double[] alpha = new double[n - 1];
        double[] beta = new double[n - 1];
        double A=0;
        double B=0;
        double C=0;
        double F=0;
        double h_i;
        double h_i1;
        double z;
        alpha[0] = beta[0] = 0.0;
        for (int i = 1; i < n - 1; ++i)
        {
            h_i = x[i] - x[i - 1];
            h_i1 = x[i + 1] - x[i];
            A = h_i;
            C = 2.0 * (h_i + h_i1);
            B = h_i1;
            F = 6.0 * ((y[i + 1] - y[i]) / h_i1 - (y[i] - y[i - 1]) / h_i);
            z = (A * alpha[i - 1] + C);
            alpha[i] = -B / z;
            beta[i] = (F - A * beta[i - 1]) / z;
        }

        splines[n - 1].c = (F - A * beta[n - 2]) / (C + A * alpha[n - 2]);

        // Нахождение решения - обратный ход метода прогонки
        for (int i = n - 2; i > 0; --i)
        {
            splines[i].c = alpha[i] * splines[i + 1].c + beta[i];
        }

        // Освобождение памяти, занимаемой прогоночными коэффициентами
        beta = null;
        alpha = null;

        // По известным коэффициентам c[i] находим значения b[i] и d[i]
        for (int i = n - 1; i > 0; --i)
        {
             h_i = x[i] - x[i - 1];
            splines[i].d = (splines[i].c - splines[i - 1].c) / h_i;
            splines[i].b = h_i * (2.0 * splines[i].c + splines[i - 1].c) / 6.0 + (y[i] - y[i - 1]) / h_i;
        }
    }

    // Вычисление значения интерполированной функции в произвольной точке
    public double f(double x) {

        spline_tuple s;
        int n= splines.length;
//BuildSpline(myx,y,n);
        if (x <= splines[0].x) // Если x меньше точки сетки x[0] - пользуемся первым эл-тов массива
             s = splines[1];
        else if (x >= splines[n - 1].x) // Если x больше точки сетки x[n - 1] - пользуемся последним эл-том массива
            s = splines[n - 1];
        else // Иначе x лежит между граничными точками сетки - производим бинарный поиск нужного эл-та массива
        {
            int i = 0, j = n - 1;
            while (i + 1 < j) {
                int k = i + (j - i) / 2;
                if (x <= splines[k].x) j = k;
                else i = k;
            }
                s = splines[j];
        }
         double dx = (x - s.x); // Вычисляем значение сплайна в заданной точке по схеме Горнера
         return s.a + (s.b + (s.c / 2.0 + s.d * dx / 6.0) * dx) * dx;
    }
}

