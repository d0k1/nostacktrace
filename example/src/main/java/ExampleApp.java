/**
 * Created by doki on 09.11.16.
 */
public class ExampleApp {


    public static void main(String[] args) throws Exception {
        new ExampleApp().foo();
    }

    private void foo() throws Exception {
        bar();
    }

    private void bar() throws Exception {
        zzz();
    }

    private void zzz() throws Exception {
        xxx();
    }

    private void xxx() throws Exception {
        ccc();
    }

    private void ccc() throws Exception {
        vvv();
    }

    private void vvv() throws Exception {
        bbb();
    }

    private void bbb() throws Exception {
        throw new Exception();
    }
}
