package com.nttdata.bootcamp.s01accountservice.repository;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.nttdata.bootcamp.s01accountservice.document.AccountDocument;
import com.nttdata.bootcamp.s01accountservice.model.AccountCreateInput.TypeEnum;
import com.nttdata.bootcamp.s01accountservice.model.AccountDetails;

/**
 * Interfaz que define un AccountMongoRepository para AccountDocument.
 * */
public interface AccountMongoRepository extends MongoRepository<AccountDocument, String> {
	/**
	 * AccountMongoRepository.
	 * @param objectId búsqueda por id.
	 * @return un objeto AccountDocument.
	 */
	AccountDocument findById(ObjectId objectId);

	/**
	 * AccountMongoRepository.
	 * @param collect busqueda por id.
	 * @return una lista del objeto AccountDocument.
	 */
	List<AccountDetails> findByOwnerClientsContains(List<ObjectId> collect);

	/**
	 * AccountMongoRepository.
	 * @param corriente busqueda por id.
	 * @param objectId objectId por id.
	 * @return una lista del objeto AccountDocument.
	 */
	List<AccountDocument> findByOwnerClientsContainsAndType(ObjectId objectId, TypeEnum corriente);

	/**
	 * AccountMongoRepository.
	 * @param idList busqueda por id.
	 * @return una lista del objeto AccountDetails.
	 */
	List<AccountDetails> findAllById(List<ObjectId> idList);
}
