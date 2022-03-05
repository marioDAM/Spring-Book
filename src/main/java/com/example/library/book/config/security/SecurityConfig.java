package com.example.library.book.config.security;

import com.example.library.book.config.APIConfig;
import com.example.library.book.config.security.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
// Activamos la seguridad a nivel de método, por si queremos trabajar a nivel de controlador
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    /*
    Mecanismos de autentificación
    Expone nuestro mecanismos de autentificación como un bean para que luego lo podamos usar en un filtro
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    /*
    Configuración de autorización según verbos y rutas
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                // Para el establecimiento de sesiones son estado, no usamos sesiones
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Autorizamos con roles y acceso
                .authorizeRequests()

                // Registrarse todos y loguearse todos. De esta manera podemos permitir las consultas a todas las rutas
                .antMatchers(HttpMethod.POST, APIConfig.API_PATH + "/clients/**").permitAll()
                .antMatchers(HttpMethod.GET, APIConfig.API_PATH + "/clients/**").hasAnyRole("USER")

                // Permitimos el acceso a todas las rutas de /rest/productos
                .antMatchers(HttpMethod.GET, APIConfig.API_PATH + "/books/**").permitAll()
                .antMatchers(HttpMethod.POST, APIConfig.API_PATH + "/books/**").permitAll()
                .antMatchers(HttpMethod.PUT, APIConfig.API_PATH + "/books/**").permitAll()
                .antMatchers(HttpMethod.DELETE, APIConfig.API_PATH + "/books/**").permitAll()
                // Permitimos el acceso a todas las rutas de /rest/files
                // Loguearse, si estyuviese en otra ruta
                //.antMatchers(HttpMethod.POST, APIConfig.API_PATH + "/auth/login").permitAll()

                // Jugamos ahora con /auth/productos

                // Consultar productos solo los clients registrados pueden hacerlo
                .antMatchers(HttpMethod.GET, APIConfig.API_PATH + "/auth/reservations/**").hasAnyRole("USER")
                // Añadir productos solo los administradores
                // Actualizar productos solo los clients
                .antMatchers(HttpMethod.PUT, APIConfig.API_PATH + "/auth/reservations/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, APIConfig.API_PATH + "/auth/reservations/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, APIConfig.API_PATH + "/auth/reservations/**").hasRole("USER")


                // Eliminar productos solo el administrador

                // Cuidado que la ruta a la consola de H2 está capada, debemos darle acceso a todos temporalmente
                //.antMatchers("/h2-console").permitAll()
                // De la misma manera a swagger
                //.antMatchers("/swagger-ui.html").permitAll()

                // Resto de rutas, auteticadas o no, tu decides, cuidado con la consola
                // de H2
                // y la documentación de Swagger
                // que debes darle acceso si la necesitas.
                //.anyRequest().authenticated();
                // O no, depende de nuestra política de seguridad
                .anyRequest().not().authenticated();

        // De nuevo para la consola de H2 estas dos lineas
//                .and().csrf().ignoringAntMatchers("/h2-console/**")
//                .and().headers().frameOptions().sameOrigin();


        // Será el encargado de coger el token y si es válido lo dejaremos pasar...
        // Añadimos el filtro (jwtAuthorizationFilter).
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

    }


}
