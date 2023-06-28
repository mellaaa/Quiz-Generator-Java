# tema-1-mellaaa
tema-1-mellaaa created by GitHub Classroom
/*MATEICA ELENA ANDREEA ALEXANDRA 323CB /*

-----GENERATOR DE CHESTIONARE POO 2022------

    Pentru fiecare functie in parte am verificat cu ajutorul functiei CredentialsValidation
daca user-ul exista si daca combinatia user-parola este in fisierul potrivit iterand prin 
fisierul users.csv
    Pentru a face cleanup se sterge fiecare fisier pe rand, se verifica stergea si daca 
totul este in regula se afiseaza mesajul potrivit, de asemenea variabilele pentru
contorizarea id-urilor sunt resetate la valoarea 1.
    Pentru useri se itereaza prin fisier si se verifica daca exista deja, in caz afirmativ 
se afiseaza eroare de user exista deja. Daca nu a fost gasit iar parola este in regula ( exista
parametru cu "-p" user-ul se insereaza in users.csv.
    Pentru intrebari se fac verificarile obisnuite (numar de raspunsuri, parola user) si de altfel
altele precum: intrebare single are mai multe raspunsuri corecte, raspunsuri identice etc. prin
apelarea metodelor corespunzatoare. Raspunsurile la o intreabre sunt stocare astfel : 
"text @ valoarea de adevar @ id".
    Pentru comanda get-question-id-by-text ne folosim de o metoda care primeste text-ul unei 
intrebari, itereaza prin fisier si intoarce id-ul intrebarii cu acel text.
    Pentru functia get-all-questions iteram prin fisierul de intrebari iar pentru fiecare 
afisam informatiile. Asemenea se procedeaza si pentru get-all-quizzez
    Pentru crearea unui quiz se scriu in fisierul quiz.csv infomatiile (user, parola, id, 
id-urile intrebarilor pentru a fi cautate in fisierul de intrebari).
    Stergerea unui quiz se foloseste de un fisier auxiliar unde sunt inserate toate celelalte
quiz uri mai putin cel ce trebuie sters,
    apoi fisierul de quiz-uri este creat din nou si restaurat cu intrebarile din fisierul auxiliar.
    Pentru completarea quiz-urilor se compara raspunsul dat de user si valorile de adevar ale 
intrebarii pentru a se calcula scorul.


