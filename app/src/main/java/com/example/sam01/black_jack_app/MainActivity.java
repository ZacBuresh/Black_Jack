package com.example.sam01.black_jack_app;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Random;

//Author: Zachary Buresh
//Date: 10/8/2018
//Description: This mobile application is a version of the card game "BlackJack". The user always
//             starts with $1000 and they can bet using the seek bar, radio buttons, or by entering
//             a number in the edit text. After betting, it deals the randomly generated cards
//             appropriately. At the end of each hand, a message will tell you the results. At any
//             point, the user can hit the "STOP" button to see their final results and information
//             about the hands they just played! They can then restart with a buy in amount or quit.

public class MainActivity extends AppCompatActivity {
    TextView dealerCards, yourCards, amount, seekProgress;
    String strYours, strDealer;
    Button btnBet, btnHit, btnStand, doubleDown;
    int min = 1; int max = 14;
    int yourTotal = 0; int dealerTotal = 0;
    Random card = new Random();
    JSONArray jArray = new JSONArray();
    ArrayList resultArray = new ArrayList();
    int  betAmount;
    RadioGroup radioGroup;
    RadioButton btnTen, btnFifty, btnHundred;
    EditText editBet;
    SeekBar seekBar;
    CheckBox checkBox;
    ToggleButton betting;
    static int ctRestart = 0, totalAmount = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dealerCards = (TextView) findViewById(R.id.dealerCards);
        yourCards = (TextView) findViewById(R.id.yourCards);
        seekProgress = (TextView) findViewById(R.id.seekProgress);
        btnBet = (Button) findViewById(R.id.btnBet);
        btnHit = (Button) findViewById(R.id.btnHit);
        doubleDown = (Button) findViewById(R.id.doubleDown);
        btnStand = (Button) findViewById(R.id.btnStand);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnTen = (RadioButton) findViewById(R.id.btnTen);
        btnFifty = (RadioButton) findViewById(R.id.btnFifty);
        btnHundred = (RadioButton) findViewById(R.id.btnHundred);
        editBet = (EditText) findViewById(R.id.editBet);
        createStepValueSeekbar(seekBar, 1, totalAmount, 10, 1);
        amount = (TextView) findViewById(R.id.amount);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        betting = (ToggleButton) findViewById(R.id.betting);
        amount.setText("$" + totalAmount);
    }

    //Controls the seek bar's functionality
    private void createStepValueSeekbar(View v, final int MIN, final int MAX, final int STEP, int currentValue)
    {
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(totalAmount);
        seekBar.setProgress(calculateProgress(currentValue, MIN, MAX, STEP));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                seekProgress.setText("" + progress);
                seekBar.setMax(totalAmount);
                //seekProgress.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
            }
        });
    }

    private int calculateProgress(int value, int MIN, int MAX, int STEP) {
        return (100 * (value - MIN)) / (MAX - MIN);
    }

    //Starts a new activity to show the end screen if the user chooses to stop playing.
    public void stopPlaying(View view) {
        Intent intentResults = new Intent(this, Main2Activity.class);
        String handList = jArray.toString();
        intentResults.putExtra("HandList", handList);
        intentResults.putExtra("Result", "$" + Integer.toString(totalAmount));
        startActivity(intentResults);
    }

    //Accepts the amount the user bet and deals cards to start the hand.
    public void startHand(View view) {

        int betSelected = 0;
        dealerTotal = 0;
        yourTotal = 0;
        if (betting.isChecked()) {
            if (checkBox.isChecked() == true) {
                betAmount = totalAmount;
                betSelected++;
                checkBox.setChecked(false);
            } else if (editBet.getText().toString().trim().length() != 0) {
                betAmount = Integer.parseInt(editBet.getText().toString());
                if (betAmount > totalAmount){
                    Toast.makeText(this, "Bet exceeds your total!", Toast.LENGTH_SHORT).show();
                }
                else {
                    betSelected++;
                }
                editBet.setText("");
            } else if (seekBar.getProgress() != 0) {
                betAmount = seekBar.getProgress();
                if (betAmount > totalAmount){
                    Toast.makeText(this, "Bet exceeds your total!", Toast.LENGTH_SHORT).show();
                }
                else {
                    betSelected++;
                }
                seekBar.setProgress(0);
            } else if (btnTen.isChecked() || btnFifty.isChecked() || btnHundred.isChecked()) {
                int btnId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) findViewById(btnId);
                betAmount = Integer.parseInt(selectedRadioButton.getText().toString());
                if (betAmount > totalAmount){
                    Toast.makeText(this, "Bet exceeds your total!", Toast.LENGTH_SHORT).show();
                }
                else {
                    betSelected++;
                }
                radioGroup.clearCheck();
            } else {
                Toast.makeText(this, "Enter a bet!", Toast.LENGTH_SHORT).show();
            }
            if (betSelected == 1) {
                btnBet.setEnabled(false);
                btnHit.setEnabled(true);
                btnStand.setEnabled(true);
                doubleDown.setEnabled(true);
                int firstDealer = card.nextInt(max - min) + min;
                if (firstDealer == 1) {
                    dealerTotal += 1;
                    strDealer = "A";
                } else if (firstDealer == 11) {
                    dealerTotal += 10;
                    strDealer = "J";
                } else if (firstDealer == 12) {
                    dealerTotal += 10;
                    strDealer = "Q";
                } else if (firstDealer == 13) {
                    dealerTotal += 10;
                    strDealer = "K";
                } else {
                    dealerTotal += firstDealer;
                    strDealer = Integer.toString(firstDealer);
                }

                int firstYours = card.nextInt(max - min) + min;
                if (firstYours == 1) {
                    yourTotal += 1;
                    strYours = "A";
                } else if (firstYours == 11) {
                    yourTotal += 10;
                    strYours = "J";
                } else if (firstYours == 12) {
                    yourTotal += 10;
                    strYours = "Q";
                } else if (firstYours == 13) {
                    yourTotal += 10;
                    strYours = "K";
                } else {
                    yourTotal += firstYours;
                    strYours = Integer.toString(firstYours);
                }

                int secondYours = card.nextInt(max - min) + min;
                if (secondYours == 1) {
                    yourTotal += 1;
                    strYours += " | " + "A";
                } else if (secondYours == 11) {
                    yourTotal += 10;
                    strYours += " | " + "J";
                } else if (secondYours == 12) {
                    yourTotal += 10;
                    strYours += " | " + "Q";
                } else if (secondYours == 13) {
                    yourTotal += 10;
                    strYours += " | " + "K";
                } else {
                    yourTotal += secondYours;
                    strYours += " | " + Integer.toString(secondYours);
                }
                yourCards.setText(strYours);
                dealerCards.setText(strDealer);
            }
        }
        else{
            btnBet.setEnabled(false);
            btnHit.setEnabled(true);
            btnStand.setEnabled(true);
            doubleDown.setEnabled(true);
            int firstDealer = card.nextInt(max - min) + min;
            if (firstDealer == 1) {
                dealerTotal += 1;
                strDealer = "A";
            } else if (firstDealer == 11) {
                dealerTotal += 10;
                strDealer = "J";
            } else if (firstDealer == 12) {
                dealerTotal += 10;
                strDealer = "Q";
            } else if (firstDealer == 13) {
                dealerTotal += 10;
                strDealer = "K";
            } else {
                dealerTotal += firstDealer;
                strDealer = Integer.toString(firstDealer);
            }

            int firstYours = card.nextInt(max - min) + min;
            if (firstYours == 1) {
                yourTotal += 1;
                strYours = "A";
            } else if (firstYours == 11) {
                yourTotal += 10;
                strYours = "J";
            } else if (firstYours == 12) {
                yourTotal += 10;
                strYours = "Q";
            } else if (firstYours == 13) {
                yourTotal += 10;
                strYours = "K";
            } else {
                yourTotal += firstYours;
                strYours = Integer.toString(firstYours);
            }

            int secondYours = card.nextInt(max - min) + min;
            if (secondYours == 1) {
                yourTotal += 1;
                strYours += " | " + "A";
            } else if (secondYours == 11) {
                yourTotal += 10;
                strYours += " | " + "J";
            } else if (secondYours == 12) {
                yourTotal += 10;
                strYours += " | " + "Q";
            } else if (secondYours == 13) {
                yourTotal += 10;
                strYours += " | " + "K";
            } else {
                yourTotal += secondYours;
                strYours += " | " + Integer.toString(secondYours);
            }
            yourCards.setText(strYours);
            dealerCards.setText(strDealer);
        }
        editBet.setText("");
    }

    //Allows the user to hit in blackjack.
    public void hit(View view) {
        int hitCard = card.nextInt(max-min) + min;
        if (hitCard == 1) {
            yourTotal += 1;
            strYours += " | " + "A";
        }
        else if (hitCard == 11) {
            yourTotal += 10;
            strYours += " | " + "J";
        }
        else if (hitCard == 12){
            yourTotal += 10;
            strYours += " | " + "Q";
        }
        else if (hitCard == 13){
            yourTotal += 10;
            strYours += " | " + "K";
        }
        else{
            yourTotal += hitCard;
            strYours += " | " + Integer.toString(hitCard);
        }
        yourCards.setText(strYours);
        if (betting.isChecked()) {
            if (yourTotal > 21) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        yourCards.setText("BUST" + "     (" + yourTotal + ")");
                        dealerCards.setText("YOU LOSE!" + "     (" + dealerTotal + ")");
                    }
                }, 1000);
                btnBet.setEnabled(true);
                btnHit.setEnabled(false);
                btnStand.setEnabled(false);
                doubleDown.setEnabled(false);
                totalAmount -= betAmount;
                String earnings = "-" + Integer.toString(betAmount);
                Hand newHand = new Hand(strYours, strDealer, false, earnings);
                amount.setText("$" + totalAmount);
                jArray.put(newHand);
                if (totalAmount <= 0){
                    Intent intentResults = new Intent(this, Main2Activity.class);
                    String handList = jArray.toString();
                    intentResults.putExtra("HandList", handList);
                    intentResults.putExtra("Result", "$0");
                    startActivity(intentResults);
                }
            }
            if (yourTotal == 21) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        yourCards.setText("BLACK JACK!");
                        dealerCards.setText("BLACK JACK!");
                    }
                }, 1000);
                btnBet.setEnabled(true);
                btnHit.setEnabled(false);
                btnStand.setEnabled(false);
                doubleDown.setEnabled(false);
                totalAmount += (betAmount * 1.5);
                double blackJack = betAmount * 1.5;
                String earnings = "+" + Integer.toString((int) blackJack);
                Hand newHand = new Hand(strYours, strDealer, true, earnings);
                amount.setText("$" + totalAmount);
                jArray.put(newHand);
            }
        }
        else{
            if (yourTotal > 21) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        yourCards.setText("BUST" + "     (" + yourTotal + ")");
                        dealerCards.setText("YOU LOSE!" + "     (" + dealerTotal + ")");
                    }
                }, 1000);
                btnBet.setEnabled(true);
                btnHit.setEnabled(false);
                btnStand.setEnabled(false);
                doubleDown.setEnabled(false);
                Hand newHand = new Hand(strYours, strDealer, false, "0");
                jArray.put(newHand);
            }
            if (yourTotal == 21) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        yourCards.setText("BLACK JACK!");
                        dealerCards.setText("BLACK JACK!");
                    }
                }, 1000);
                btnBet.setEnabled(true);
                btnHit.setEnabled(false);
                btnStand.setEnabled(false);
                doubleDown.setEnabled(false);
                Hand newHand = new Hand(strYours, strDealer, true, "0");
                jArray.put(newHand);
            }
        }
    }

    //Allows the user to stand in blackjack.
    public void stand(View view) {
        btnBet.setEnabled(true);
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        doubleDown.setEnabled(false);
        final Handler handler = new Handler();
        if (betting.isChecked()) {
            while (dealerTotal <= 21) {
                int newDealerCard = card.nextInt(max - min) + min;
                if (newDealerCard == 1) {
                    dealerTotal += 1;
                    strDealer += " | " + "A";
                } else if (newDealerCard == 11) {
                    dealerTotal += 10;
                    strDealer += " | " + "J";
                } else if (newDealerCard == 12) {
                    dealerTotal += 10;
                    strDealer += " | " + "Q";
                } else if (newDealerCard == 13) {
                    dealerTotal += 10;
                    strDealer += " | " + "K";
                } else {
                    dealerTotal += newDealerCard;
                    strDealer += " | " + Integer.toString(newDealerCard);
                }

                if (dealerTotal >= 17 && dealerTotal <= 21) {
                    if (yourTotal > dealerTotal) {
                        yourCards.setText(strYours);
                        dealerCards.setText(strDealer);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCards.setText("YOU WIN!" + "     (" + dealerTotal + ")");
                                yourCards.setText("YOU WIN!" + "     (" + yourTotal + ")");
                            }
                        }, 2000);
                        totalAmount += betAmount;
                        String earnings = "+" + Integer.toString(betAmount);
                        Hand newHand = new Hand(strYours, strDealer, true, earnings);
                        amount.setText("$" + totalAmount);
                        jArray.put(newHand);
                        break;
                    } else if (yourTotal == dealerTotal) {
                        yourCards.setText(strYours);
                        dealerCards.setText(strDealer);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCards.setText("PUSH!" + "     (" + dealerTotal + ")");
                                yourCards.setText("PUSH!" + "     (" + yourTotal + ")");
                            }
                        }, 2000);
                        String earnings = "0";
                        Hand newHand = new Hand(strYours, strDealer, null, earnings);
                        amount.setText("$" + totalAmount);
                        jArray.put(newHand);
                        break;
                    } else {
                        yourCards.setText(strYours);
                        dealerCards.setText(strDealer);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCards.setText("YOU LOSE!" + "     (" + dealerTotal + ")");
                                yourCards.setText("YOU LOSE!" + "     (" + yourTotal + ")");
                            }
                        }, 2000);
                        totalAmount -= betAmount;
                        String earnings = "-" + Integer.toString(betAmount);
                        Hand newHand = new Hand(strYours, strDealer, false, earnings);
                        amount.setText("$" + totalAmount);
                        jArray.put(newHand);
                        if (totalAmount <= 0){
                            Intent intentResults = new Intent(this, Main2Activity.class);
                            String handList = jArray.toString();
                            intentResults.putExtra("HandList", handList);
                            intentResults.putExtra("Result", "$0");
                            startActivity(intentResults);
                        }
                        break;
                    }
                }
            }
            if (dealerTotal > 21) {
                yourCards.setText(strYours);
                dealerCards.setText(strDealer);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dealerCards.setText("BUST" + "     (" + dealerTotal + ")");
                        yourCards.setText("YOU WIN!" + "     (" + yourTotal + ")");
                    }
                }, 2000);
                totalAmount += betAmount;
                String earnings = "+" + Integer.toString(betAmount);
                Hand newHand = new Hand(strYours, strDealer, true, earnings);
                amount.setText("$" + totalAmount);
                jArray.put(newHand);
            }
        }
        else{
            while (dealerTotal <= 21) {
                int newDealerCard = card.nextInt(max - min) + min;
                if (newDealerCard == 1) {
                    dealerTotal += 1;
                    strDealer += " | " + "A";
                } else if (newDealerCard == 11) {
                    dealerTotal += 10;
                    strDealer += " | " + "J";
                } else if (newDealerCard == 12) {
                    dealerTotal += 10;
                    strDealer += " | " + "Q";
                } else if (newDealerCard == 13) {
                    dealerTotal += 10;
                    strDealer += " | " + "K";
                } else {
                    dealerTotal += newDealerCard;
                    strDealer += " | " + Integer.toString(newDealerCard);
                }

                if (dealerTotal >= 17 && dealerTotal <= 21) {
                    if (yourTotal > dealerTotal) {
                        yourCards.setText(strYours);
                        dealerCards.setText(strDealer);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCards.setText("YOU WIN!" + "     (" + dealerTotal + ")");
                                yourCards.setText("YOU WIN!" + "     (" + yourTotal + ")");
                            }
                        }, 2000);
                        Hand newHand = new Hand(strYours, strDealer, true, "");
                        jArray.put(newHand);
                        break;
                    } else if (yourTotal == dealerTotal) {
                        yourCards.setText(strYours);
                        dealerCards.setText(strDealer);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCards.setText("PUSH!" + "     (" + dealerTotal + ")");
                                yourCards.setText("PUSH!" + "     (" + yourTotal + ")");
                            }
                        }, 2000);
                        String earnings = "0";
                        Hand newHand = new Hand(strYours, strDealer, null, earnings);
                        jArray.put(newHand);
                        break;
                    } else {
                        yourCards.setText(strYours);
                        dealerCards.setText(strDealer);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dealerCards.setText("YOU LOSE!" + "     (" + dealerTotal + ")");
                                yourCards.setText("YOU LOSE!" + "     (" + yourTotal + ")");
                            }
                        }, 2000);
                        Hand newHand = new Hand(strYours, strDealer, false, "0");
                        jArray.put(newHand);
                        break;
                    }
                }
            }
            if (dealerTotal > 21) {
                yourCards.setText(strYours);
                dealerCards.setText(strDealer);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dealerCards.setText("BUST" + "     (" + dealerTotal + ")");
                        yourCards.setText("YOU WIN!" + "     (" + yourTotal + ")");
                    }
                }, 2000);
                Hand newHand = new Hand(strYours, strDealer, true, "0");
                jArray.put(newHand);
            }
        }
    }

    //Allows the user to double down in blackjack.
    public void doubleDown(View view) {
        betAmount *= 2;
        if (betAmount >= totalAmount){
            Toast.makeText(this, "Bet exceeds your total!", Toast.LENGTH_SHORT).show();
        }
        else {
            btnHit.callOnClick();
            if (yourTotal <= 21) {
                btnStand.callOnClick();
            }
        }
    }
}
