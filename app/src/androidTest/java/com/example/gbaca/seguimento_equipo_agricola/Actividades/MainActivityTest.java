package com.example.gbaca.seguimento_equipo_agricola.Actividades;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.gbaca.seguimento_equipo_agricola.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnBajarMaestro), withText("BAJAR MAESTROS"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.Button01), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextSearch), isDisplayed()));
        appCompatEditText.perform(replaceText("y"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btnActivar), withText("ACTIVAR"),
                        withParent(allOf(withId(R.id.contenedor),
                                withParent(withId(R.id.recyclerviewOS)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.btnImplemento1), isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextSearch), isDisplayed()));
        appCompatEditText2.perform(replaceText("a"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btnActivar), withText("ACTIVAR"),
                        withParent(allOf(withId(R.id.contenedor),
                                withParent(withId(R.id.recyclerviewImplementos)))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.btnImplemento2), isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextSearch), isDisplayed()));
        appCompatEditText3.perform(replaceText("m"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btnActivar), withText("ACTIVAR"),
                        withParent(allOf(withId(R.id.contenedor),
                                withParent(withId(R.id.recyclerviewImplementos)))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.btnImplemento3), isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextSearch), isDisplayed()));
        appCompatEditText4.perform(replaceText("p"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.btnActivar), withText("ACTIVAR"),
                        withParent(allOf(withId(R.id.contenedor),
                                withParent(withId(R.id.recyclerviewImplementos)))),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.btnFrente), withText("N/D"), isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editTextSearch), isDisplayed()));
        appCompatEditText5.perform(replaceText("m"), closeSoftKeyboard());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.btnActivar), withText("ACTIVAR"),
                        withParent(allOf(withId(R.id.contenedor),
                                withParent(withId(R.id.recyclerviewFrentes)))),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.btnCodigoEmpleado), withText("N/D"), isDisplayed()));
        appCompatButton8.perform(click());

    }

}
