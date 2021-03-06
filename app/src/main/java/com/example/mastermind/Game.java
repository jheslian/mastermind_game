package com.example.mastermind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Game extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener{
    String choice1 = "", choice2 = "", choice3 = "", choice4 = "";
    String[] user_input = {choice1, choice2, choice3, choice4};

    // list of available fruit
    Fruit Banana = new Fruit("Banana", false, true, R.drawable.banana);
    Fruit Kiwi = new Fruit("Kiwi", false, true, R.drawable.kiwi);
    Fruit Strawberry = new Fruit("Strawberry", false, false, R.drawable.strawberry);
    Fruit Raspberry = new Fruit("Raspberry", false, false, R.drawable.raspberry);
    Fruit Grapes = new Fruit("Grapes", true, false, R.drawable.grapes);
    Fruit Orange = new Fruit("Orange", false, true, R.drawable.orange);
    Fruit Lemon = new Fruit("Lemon", false, true, R.drawable.lemon);
    Fruit Plum = new Fruit("Plum", true, false, R.drawable.plum);

    // Fruit basket = Fruits
    Fruit[] Fruits = {Banana, Kiwi, Strawberry, Raspberry, Grapes, Orange, Lemon, Plum};
    Fruit[] generated_answer = generate_answer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Integer[] test_user = {2, 2, 2, 2};
        check_if_is_won(test_user);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fruits_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);


        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(this);

        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapter);
        spinner3.setOnItemSelectedListener(this);

        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner4.setAdapter(adapter);
        spinner4.setOnItemSelectedListener(this);


        Button submit_button = findViewById(R.id.submit_user_input);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer[] hints = user_attempt_checker(generate_answer(), user_input);
                RecyclerView rvFruits = findViewById(R.id.rvFruits);
                Fruit fruit1 = null;
                Fruit fruit2 = null;
                Fruit fruit3 = null;
                Fruit fruit4 = null;
                // Fruit fruit1 = Fruits[(Arrays.asList(Fruits)).indexOf(user_input[0])];
                for (Fruit fruit:Fruits) {
                    if(fruit.getName().equals(user_input[0])){
                        fruit1 = fruit;
                    }
                    if(fruit.getName().equals(user_input[1])){
                        fruit2 = fruit;
                    }
                    if(fruit.getName().equals(user_input[2])){
                        fruit3 = fruit;
                    }
                    if(fruit.getName().equals(user_input[3])){
                        fruit4 = fruit;
                    }
                }
                FruitAdapter adapter = new FruitAdapter(fruit1,fruit2,fruit3,fruit4,hints[0],hints[1],hints[2],hints[3]);
                rvFruits.setAdapter(adapter);
                rvFruits.setLayoutManager(new LinearLayoutManager(Game.this));
            }
        });

    }

    public Fruit[] generate_answer() {
        int inserted_fruit = 0;
        Fruit[] result = new Fruit[4];

        while (inserted_fruit != 4) {
            Random rnd = new Random();
            int proposition = rnd.nextInt(Fruits.length);
            boolean isValid = true;

            for (int i = 0; i < result.length; i++) {
                if (result[i] != null) {
                    if (result[i].getName().equals(Fruits[proposition].getName())) {
                        isValid = false;
                        break;
                    }
                }
            }

            if (isValid) {
                result[inserted_fruit] = Fruits[proposition];
                inserted_fruit++;
            }
        }
        return result;
    }

    /**
     * Function who take a generated answer in argument and check if the attempt is ok
     * p_generated_answer : returned by generate_answer()
     * p_user_input : an array of string who represent the user answer
     * return :
     *
     * @return an array of integer
     */
    public Integer[] user_attempt_checker(Fruit[] p_generated_answer, String[] p_user_input) {
        Integer[] result = new Integer[4];
        // on boucle sur les resultats de l'utilisateur
        // si le fruit est absent on met 0 dans un tableau
        for (int i = 0; i < p_user_input.length; i++) {
            int checked_result = 0;
            for (int j = 0; j < p_generated_answer.length; j++) {
                if (p_user_input[i].equals(p_generated_answer[j].getName())) {
                    if (i == j) {
                        // si le fruit est pr??sent et bien plac?? on met 2 dans un tableau
                        checked_result = 2;
                    } else {
                        // si le fruit est pr??sent on met 1 dans un tableau
                        checked_result = 1;
                    }
                }
            }
            result[i] = checked_result;
        }
        // on fait un sort() descendant sur le tableau
        Arrays.sort(result, Collections.reverseOrder());
        // on retourne le tableau ex: [2,1,0,0]
        return result;
    }

    public String check_if_is_won(Integer[] user_attempt) {
        // on v??rifie le tableau
        String result = "Je ne sais pas";
        Integer[] won = {2, 2, 2, 2};
        int available_attempt = 10;
        // si gagn?? => menu gagn??
        if (Arrays.equals(user_attempt, won)) {
            result = "Gagn??";
        }
        // sinon
        else {
            if (available_attempt > 0) {
                // si reste des essais => on refait une proposition
                result = "Essaie encore";
            } else {
                // sinon perdu
                result = "Perdu";
            }
        }
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (parent.getId()) {
            case R.id.spinner1:
                user_input[0] = text;
                break;
            case R.id.spinner2:
                user_input[1] = text;
                break;
            case R.id.spinner3:
                user_input[2] = text;
                break;
            case R.id.spinner4:
                user_input[3] = text;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}