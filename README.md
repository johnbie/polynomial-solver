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
 - Visual component and summary are all included by default as long as the user loads in a polynomial. (NOTE: these are some of the extra actions)
 - To create or load a polynomial, go under Files menu and Load from list (Ctrl-L) or create new (Ctrl-N). (LOAD FUNCTION)
 - To save the one currently loaded, click Save to file (Ctrl-S). (SAVE FUNCTION)
 - You can also add new terms to the existing polynomial in the Edit menu (Ctrl-T). (NOTE: this is your action 1)
 - You can even view the derivative of current polynomial or reset it. (NOTE: these are also some of the extra actions)