package Giuseppe.DigitalDelights.shippingdetails;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, UUID> {

}
