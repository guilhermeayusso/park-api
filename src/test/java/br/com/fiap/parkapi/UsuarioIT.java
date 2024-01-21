package br.com.fiap.parkapi;

import br.com.fiap.parkapi.exception.ErrorMessage;
import br.com.fiap.parkapi.web.dto.request.UsuarioCreateRequestDto;
import br.com.fiap.parkapi.web.dto.request.UsuarioSenhaRequestDto;
import br.com.fiap.parkapi.web.dto.response.UsuarioCreateResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@Sql(scripts = "/sql/usuarios/usuario-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuario-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createUsuario_ComDadosOK_ReturnStatus201() {
        UsuarioCreateResponseDto usuarioCreateResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateRequestDto("tody@email.com", "2020@Agost"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioCreateResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(usuarioCreateResponseDto).isNotNull();
        Assertions.assertThat(usuarioCreateResponseDto.getId()).isNotNull();
        Assertions.assertThat(usuarioCreateResponseDto.getUsername()).isEqualTo("tody@email.com");
        Assertions.assertThat(usuarioCreateResponseDto.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void createUsuario_ComUsernameInvalido_ReturnStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateRequestDto("", "2020@Agost"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateRequestDto("tody@", "2020@Agost"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateRequestDto("tody@email", "2020@Agost"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUsuario_ComPasswordInvalido_ReturnStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateRequestDto("tody@email.com", "2020@Agost1234567890755443"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateRequestDto("tody@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateRequestDto("tody@email.com", "2020"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUsuario_ComUsernameJaExistente_ReturnStatus409() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateRequestDto("neymar@gmail.com", "2020@Agost"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
        }

    @Test
    public void buscarUsuario_ComIdCorreto_ReturnStatus200() {
        UsuarioCreateResponseDto responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient,"guizao@gmail.com", "123456789"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioCreateResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isEqualTo(100);
        Assertions.assertThat(responseBody.getUsername()).isEqualTo("guizao@gmail.com");
        Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios/102")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "guizao@gmail.com", "123456789"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioCreateResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(102);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bob@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");


        responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios/102")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bob@gmail.com", "123456789"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioCreateResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(102);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bob@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void buscarUsuario_ComIdInexistente_ReturnStatus404() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios/600")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient,"guizao@gmail.com", "123456789"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    public void buscarUsuario_ComUsuarioClienteBuscandoOutroCliente_RetornarErrorMessageComStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios/103")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bob@gmail.com", "123456789"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void editarSenha_ComDadosValidos_ReturnStatus204() {
        webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient,"guizao@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaRequestDto("123456789","999999999","999999999"))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .patch()
                .uri("/api/v1/usuarios/102")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient,"bob@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaRequestDto("123456789","999999999","999999999"))
                .exchange()
                .expectStatus().isNoContent();

    }

    @Test
    public void editarSenha_ComUsuariosDiferentes_RetornarErrorMessageComStatus403() {
        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/0")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "guizao@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaRequestDto("123456789", "999999999", "999999999"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

       Assertions.assertThat(responseBody).isNotNull();
       Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/0")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bob@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaRequestDto("123456789", "999999999", "999999999"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void editarSenha_ComDadosInvalidos_ReturnStatus422() {
        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "guizao@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaRequestDto("","",""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "guizao@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaRequestDto("123456789","12","12"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "guizao@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaRequestDto("123456789","1233333333333333333333333","1233333333333333333333333"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    }

    @Test
    public void editarSenha_ComSenhaQueNaoConfere_ReturnStatus400() {
        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "guizao@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaRequestDto("123456789", "123456787", "123456786"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "guizao@gmail.com", "123456789"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaRequestDto("123456788", "123456787", "123456787"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void listarUsuarios_SemParametro_ResturnStatus200(){
       List<UsuarioCreateResponseDto> responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "guizao@gmail.com", "123456789"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioCreateResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.size()).isEqualTo(3);
    }

    @Test
    public void listarUsuarios_ComUsuarioSemPermissao_RetornarErrorMessageComStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/usuarios")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bob@gmail.com", "123456789"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
}
