import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final BigDecimal ONE = BigDecimal.ONE;
    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal FOUR = new BigDecimal(4);

    public static void main(String[] args) {

        long debProg = System.currentTimeMillis();

        int scale;

        if(args.length > 0) {
            try {
                scale = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                scale = 1000;
            }
        }
        else {
            scale = 100;
        }

        BigDecimal a0 = ONE;
        BigDecimal b0 = ONE.divide(sqrt(TWO, scale), scale, BigDecimal.ROUND_HALF_UP);
        BigDecimal t0 = new BigDecimal(0.25);
        BigDecimal p0 = ONE;

        BigDecimal pi = ONE, tmp;

        long deb = System.currentTimeMillis();

        while(!a0.equals(b0)) {
            tmp = a0;
            a0 = a0.add(b0).divide(TWO, scale, BigDecimal.ROUND_HALF_UP);
            b0 = sqrt(tmp.multiply(b0), scale);
            t0 = t0.subtract(p0.multiply(a0.subtract(tmp).pow(2)));
            p0 = p0.multiply(TWO);

            pi = a0.add(b0).pow(2).divide(t0.multiply(FOUR), scale, BigDecimal.ROUND_HALF_UP);

        }

        long fin = System.currentTimeMillis();

        String temps;

        if((fin - deb) < 1000) {
            temps = "Generated " + (fin - deb) + " milliseconds\r\n";
        }
        else {
            temps = "Generated " + ((fin - deb) / 1000) + " seconds and " + ((fin - deb) - ((fin - deb) / 1000 * 1000)) + " milliseconds\r\n";
        }

        Path p = Paths.get("pi-" + scale + ".txt");

        try {
            if(Files.exists(p))
                Files.delete(p);

            Files.createFile(p);

            Files.write(p, (temps + pi.toString()).getBytes());

        }
        catch (Exception e) {
            System.err.println("Error creating the file!");
        }
        finally {
            System.out.println("Calculated in : " + (System.currentTimeMillis() - debProg) + " milliseconds");
        }

    }

    public static BigDecimal sqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));

        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, BigDecimal.ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, SCALE, BigDecimal.ROUND_HALF_UP);
        }

        return x1;
    }

}
