package ac.kr.smu.prlab_server.controller

import ac.kr.smu.prlab_server.domain.User
import ac.kr.smu.prlab_server.jwt.JWTTokenProvider
import ac.kr.smu.prlab_server.service.IDTokenService
import ac.kr.smu.prlab_server.service.OAuthService
import ac.kr.smu.prlab_server.service.UserService
import com.nimbusds.jose.shaded.gson.Gson
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClientResponseException

@RequestMapping("/users")
@RestController
class UserController(
    private val service: UserService,
    private val tokenProvider: JWTTokenProvider
) {
    @PostMapping
    fun postUser(@RequestBody user: User): ResponseEntity<Any> {
        return saveUser(user)
    }

    private fun saveUser(user: User): ResponseEntity<Any> {
        val id = service.findIdByEmail(user.email)

        when {
            id == null -> {
                service.save(user)
                return ResponseEntity(HttpStatus.CREATED)
            }

            else -> return ResponseEntity(HttpStatus.CONFLICT)
        }
    }
    @GetMapping(params = ["email"])
    fun getUserByEmail(@RequestParam("email") email: String): ResponseEntity<Any> {
        val id = service.findIdByEmail(email)

        when {
            id == null -> return ResponseEntity.notFound().build()
            else -> return ResponseEntity.ok(mapOf("id" to id))
        }
    }

    @GetMapping("check-duplication")
    fun checkDuplication(@RequestParam("id") id: String): ResponseEntity<Void> {
        when {
            service.isIdExist(id) -> return ResponseEntity.noContent().build()
            else -> return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("check-email")
    fun checkEmail(
        @RequestParam("id") id: String,
        @RequestParam("email") email: String
    ): ResponseEntity<Any> {
        val user = service.findById(id)

        when {
            user == null -> return ResponseEntity.notFound().build()
            email != user.email -> return ResponseEntity(HttpStatus.CONFLICT)
            else -> return ResponseEntity.ok(mapOf("token" to tokenProvider.createToken(id, 1000L * 60 * 5)))
        }
    }

    @PatchMapping
    fun patchUserPassword(
        @RequestBody json: HashMap<String, String>,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Void> {
        val id = user.id
        val password = json["password"]

        when {
            password == null -> return ResponseEntity.badRequest().build()
            else -> {
                service.changePassword(id, password)
                return ResponseEntity.noContent().build()
            }
        }
    }

    @DeleteMapping
    fun deleteUser(@AuthenticationPrincipal user: User): ResponseEntity<Void>{
        service.deleteUser(user.id)

        return ResponseEntity.noContent().build()
    }
}