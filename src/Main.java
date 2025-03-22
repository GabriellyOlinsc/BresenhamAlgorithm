public class Main {
    static void bresenham(int x1, int y1, int x2, int y2)
    {
        int dx = x2 - x1;  //Distância entre o ponto x1 e x2
        int dy = y2 - y1;  //Distância entre o ponto y1 e y2
        int m_new = 2 * dy;  //Valor que vai simular a inclinação da reta
        int slope_error_new = m_new - dx; //variável d erro que vai definir quando incrementar y

        System.out.println("Pontos da linha:");
        for (int x = x1, y = y1; x <= x2; x++) {
            System.out.println(" (" + x + ", " + y + ")");

            // Add slope to increment angle formed
            slope_error_new += m_new;

            // Slope error reached limit, time to
            // increment y and update slope error.
            if (slope_error_new >= 0) {
                y++; // Aumenta a coordenada y quando o erro ultrapassa o limite
                slope_error_new -= 2 * dx; //corrige o erro acumulado
            }
        }
    }

    public static void main(String[] args) {

        bresenham(2, 2, 10, 5);
    }
}