# Opis projektu

## Sposób uruchamiania
* kompilacja i uruchomienie serwera odbywa się przez uruchomienie skryptu `run-project.sh`
* uruchomienie testów odbywa się przez uruchomienie skryptu `test-reservations.sh` - przechodzi on cały use-case - losowo wybiera screening, dodatkowo przechodzi przez różne przypadki błędnych zapytań

## Użyte technologie
* Java 17
* Spring Boot
* H2 jako baza do przechowywania wszystkich danych w systemie (w repozytorium umieszczony jest również docker-compose z definicją Postgresa, ponieważ testowałem lokalnie różne zapytania)

## Dodatkowe informacje
* Api przymuje request rezerwacji w postaci:
  * screeningId
  * name
  * surname
  * mapa <nr rzędu, {pierwsze miejsce do rezerwacji w rzędzie, [typy kolejnych rezerwowanych miejsc}]>

* walidacja rezerwacji jest dwuetapowa - pierwszy etap to sprawdzanie formatów podstawowych danych (np. czy imię pasuje pod wymagany regex), drugi etap to sprawdzenie poprawności logicznej zapytania (np. czy istnieje screening, rząd, siedzenie, czy jest ono wolne)

* mimo, że w treści zadania była mowa o pojedyńczym kinie, napisałem system uwzględniający wiele kin - nie wpłynęło to w żaden sposób na skomplikowanie rozwiązania, a potencjalnie łatwo jest skalować biznesowo taki system pod nowych klientów
* bazę danych inicjalizuję 1 kinem, 3 filmami, 3 salami po 2 losowe screeningi filmów na salę
