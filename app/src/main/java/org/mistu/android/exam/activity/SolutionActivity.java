package org.mistu.android.exam.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.mistu.android.exam.R;

import io.github.kexanie.library.MathView;


public class SolutionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
    }

    MathView formula_one, formula_two;
    String tex = "This come from string. You can insert inline formula:" +
            " \\(ax^2 + bx + c = 0\\) " +
            "or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$";
    String line1 = "Let, <br>\n" +
            "mole of N<sub>2</sub> = \\(x\\) <br>" +
            "mole of NO<sub>2</sub> = \\(y\\) <br>" +
            "mole of N<sub>2</sub>O<sub>4</sub> = \\(z\\)" +
            "$$\\therefore \\frac{28x+46y+92z}{1} = 55.4 \\space \\space...(i)$$";

    String line2 = "If <b>N<sub>2</sub>O<sub>4</sub> &#8594; 2N<sub>2</sub>O</b>,<br>" +
            "$$\\frac{28x_{final^{2}}+(y+2z)46}{x+y+z+z} = 39.6$$" +
            "$$\\therefore \\frac{28x+46y+92z}{1+z} = 39.6 \\space\\space...(ii)$$";

    String eqn = line1 + line2;

    @Override
    protected void onResume() {
        super.onResume();

        formula_one = (MathView) findViewById(R.id.f_one);
        formula_two = (MathView) findViewById(R.id.f_two);
        formula_one.setText(tex);
        formula_two.setText(eqn);
    }
}
