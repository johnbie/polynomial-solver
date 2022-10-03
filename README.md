# Rational Polynomials Grapher
By Jangwon Lee

## Introduction

This application summarizes polynomials provided by the user. It gives valuable informations such as the x and y intercepts, inflection points, and concave/convex regions, and it also graphs the polynomial onto the GUI. It would be used by calc 1 and 2 students, as well as anyone who's curious about polynomials. 

I created this project because I wanted to create tools that help students understand difficult mathematical concepts. I also wanted to try graphing arbitrary curves and felt that my calculus knowledge would help me do just that.


## Features

The application will give take in a rational polynomial provided by the user and output both a graph and a summary of it. The application will return values in symbolic form when possible (i.e. fractions and square roots) and decimal approximations otherwise (up to 10 decimal places).

List of traits summarized:
 - List of x intercepts (i.e. zeroes of the polynomial)
 - The y intercept
 - List of critical points (minimums, maximums, and neither)
 - List of inflection points
 - Range of increasing, decreasing, and constant regions
 - Range of concave up, concave down, and non-concave regions
 - Sign as x approaches positive and negative infinity


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
 - The curves are approximated with Bezier curves, and as such might be inaccurate in some cases. The application tries to adjust the precision based on the function's slope and the slope's rate of change.


## User Stories

List of User Stories:
 - As a high school student, I want to see where all the x/y intercepts are, and in symbolic form when possible.
 - As a high school student, I just want to know what the zeroes are for x.
 - As a college student, I want to see where all the function increases, decreases, and is concave up or down.
 - As a college student, I want to see all the critical and inflection points.
 - As a math tutor, I want to be able to quickly generate and plot the polynomials so that I can teach my students math concepts visually.