import com.bonusmart.product_api.service.event.DebeziumEventParser
import com.bonusmart.product_api.service.event.ProductIndexOrchestrator
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class ProductElasticsearchConsumer(
    private val parser: DebeziumEventParser,
    private val orchestrator: ProductIndexOrchestrator
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        topics = ["product-api.public.products"],
        groupId = "product-elasticsearch-sync"
    )
    fun consume(
        @Payload message: String,
        acknowledgment: Acknowledgment
    ) {
        runCatching {
            parser.parse(message)
                ?.also { orchestrator.handle(it) }
        }.onFailure {
            logger.error("Failed to process kafka message", it)
        }

        acknowledgment.acknowledge()
    }
}