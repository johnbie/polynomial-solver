# Rational Polynomials Grapher
By Jangwon Lee

## Introduction

This application summarizes polynomials provided by the user. It gives valuable information such as the x and y intercepts, inflection points, and concave/convex regions, and it also graphs the polynomial onto the GUI. It would be used by calc 1 and 2 students, as well as anyone who's curious about polynomials. 

I created this project because I wanted to create tools that help students understand difficult mathematical concepts. I also wanted to try graphing arbitrary curves and felt that my calculus knowledge would help me do just that.


## Features

The application will give take in a rational polynomial provided by the user and output both a graph and a summary of it. The application will return values in symbolic form when possible (i.e. fractions and square roots) and decimal approximations otherwise (up to 6 decimal places).

List of traits summarized:
 - List of x intercepts (i.e. zeroes of the polynomial)
 - The y intercept
 - List of critical points (minimums, maximums, and neither)
 - List of inflection points


There are other features that the users can enjoy, though mostly from the command line.

List of features:
 - Evaluate the function at a specific point
 - Integrate and derive the function
 - Create and save new polynomials
 - Load and modify existing polynomials
 - Create polynomials from a list of zeroes (a different kind of object)
 - Turn ordinary polynomials into factorized *rational* polynomials (if possible)


## Restrictions

Here are some list of restrictions:
 - User can specify rational values (i.e. natural numbers, fractions) as coefficients and are restricted to specifying for positive powers. 
 - When integrating a function, the user must specify the big C. 
 - While the application tries to show as much of the graph as possible, the users are limited to viewing a specific part of the graph and can neither pan nor zoom.

## User Stories

List of User Stories:
 - As a high school student, I want to see where all the x/y intercepts are, and in symbolic form when possible.
	- NOTE: the implementation doesn't yet support non-rational intercepts
 - As a college student, I want to see all the critical and inflection points.
 - As a college student, I want to just quickly get the derivative of the function.
 - As a user, I want to just check the values of the function at specific point `f(a)`.
 - As a marker/tester, I want a list of polynomials loaded in by default that I could test with.
 - As a student, I want to save a list of polynomials first before looking at each one.
- As a math tutor, I want to be able to quickly generate and plot the polynomials so that I can teach my students math concepts visually.
    - Drawing a lot of tiny lines used over implementing Bezier curves.


## Instructions for Grader
 - Visual component and summary are all included by default as long as the user loads in a polynomial.
 - To create or load a polynomial, go under Files menu and Load from list (Ctrl-L) or create new (Ctrl-N). (LOAD FUNCTION)
 - To save the one currently loaded, click Save to file (Ctrl-S). (SAVE FUNCTION)
 - You can also add new terms to the existing polynomial in the Edit menu (Ctrl-T). (NOTE: this is your action 1)
 - You can even view the derivative of current polynomial or reset it. (NOTE: these also some of the extra actions)

## Phase 4: Task 2
```
Mon Nov 28 14:48:38 PST 2022
Created a new polynomial 0

Mon Nov 28 14:48:43 PST 2022
Created a new polynomial x^3 + x

Mon Nov 28 14:48:47 PST 2022
Added 2x^2 to polynomial (now x^3 + 2x^2 + x)

Mon Nov 28 14:48:52 PST 2022
Derived polynomial (now 3x^2 + 4x + 1)

Mon Nov 28 14:48:53 PST 2022
Reset polynomial to zero polynomial
```

## Phase 4: Task 3
If I were to do any additional refactoring, I would start off by separating the math utilities class into a new `MathUtils` namespace. This would more explicitly separate out the concern so that the class files in `model` only need to worry about use cases directly related to the polynomial.

I would also reduce coupling by preventing the Roots class from calling functions in Polynomial class by creating a separate Factory class that creates the list of Roots. This new `PolynomialsFactory` class will also contain some functions from the `Polynomial` class.

Lastly, I would while also move out the many private classes in the `PolynomialGuiApp` into separate class files.