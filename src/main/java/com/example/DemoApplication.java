package com.example;

import com.sun.javafx.beans.IDProperty;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

//@RestController
//class ReservationRestController {
//    private final ReservationRepository reservationRepository;
//
//    @Autowired
//    public ReservationRestController(ReservationRepository reservationRepository) {
//        this.reservationRepository = reservationRepository;
//    }
//
//    @RequestMapping(method = RequestMethod.GET, value = "/reservations")
//    Collection<Reservation> reservations() {
//        return this.reservationRepository.findAll();
//    }
//}


@Component
class RepositoryCommandLineRunner implements CommandLineRunner {
    private final ReservationRepository reservationRepository;

    @Autowired
    public RepositoryCommandLineRunner(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Arrays.asList("Kathy", "Josh", "Ivan", "Oleg", "Vasya", "Natasha")
                .forEach(n -> this.reservationRepository.save(new Reservation(n)));

        this.reservationRepository.findAll().forEach(System.out::println);
        this.reservationRepository.findByReservationName("Vasya").forEach(System.out::println);

    }
}

@Controller
class ReservationMvcController {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationMvcController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @RequestMapping("reservations.html")
    String reservations (Model model) {
        model.addAttribute("reservations", this.reservationRepository.findAll());
        return "reservations";
    }
}

@SpringUI(path = "ui")
@Theme("valo")
class ReservationUI extends UI {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationUI(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Table table = new Table();
        table.setSizeFull();
        table.setContainerDataSource(new BeanItemContainer<>(
                Reservation.class,
                this.reservationRepository.findAll()
        ));
        setContent(table);
    }
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @RestResource(path = "by-name")
    Collection<Reservation> findByReservationName(@Param("rn") String rn);




}