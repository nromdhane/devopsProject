package tn.esprit.spring.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;

@Service
public class EntrepriseServiceImpl implements IEntrepriseService {
    private static final Logger LOG = LogManager.getLogger(EntrepriseServiceImpl.class);
    private static final String ENTREPRISEID = "Entreprise Id";
	private static final String DEPARTMENTID = "Department Id";
    @Autowired
    EntrepriseRepository entrepriseRepoistory;
    @Autowired
    DepartementRepository deptRepoistory;

    public int ajouterEntreprise(Entreprise entreprise) {
    	LOG.info(MessageFormat.format("Entreprise{0}", entreprise));
        entrepriseRepoistory.save(entreprise);
        LOG.debug(MessageFormat.format("Entreprise saved {0}", entreprise));
        LOG.debug(MessageFormat.format("{0} {1}", ENTREPRISEID, entreprise.getId()));
        return entreprise.getId();
    }

    public int ajouterDepartement(Departement dep) {
        LOG.info(MessageFormat.format("Departement{0}", dep));
        deptRepoistory.save(dep);
        LOG.debug(MessageFormat.format("Departement saved{0}", dep));
        LOG.debug(MessageFormat.format("{0}  {1}", DEPARTMENTID, dep.getId()));
        return dep.getId();
    }

    public void affecterDepartementAEntreprise(int depId, int entrepriseId) {
        // Le bout Master de cette relation N:1 est departement
        // donc il faut rajouter l'entreprise a departement
        // ==> c'est l'objet departement(le master) qui va mettre a jour
        // l'association
        // Rappel : la classe qui contient mappedBy represente le bout Slave
        // Rappel : Dans une relation oneToMany le mappedBy doit etre du cote
        // one.
        LOG.info(MessageFormat.format("{0}  {1}", ENTREPRISEID, entrepriseId));
		LOG.info(MessageFormat.format("{0}   {1}", DEPARTMENTID, depId));
		Entreprise entr = null ;
		Departement dep = null;
		Optional<Entreprise> entreprise =entrepriseRepoistory.findById(entrepriseId);
		Optional<Departement> department =deptRepoistory.findById(depId);
		if (entreprise.isPresent()&&department.isPresent()) {
            entr = entreprise.get();
            LOG.debug(MessageFormat.format("{0} {1} ", ENTREPRISEID, entrepriseId));

            dep = department.get();
            LOG.debug(MessageFormat.format("{0}{1} ", DEPARTMENTID, depId));
            dep.setEntreprise(entr);	

		LOG.info(MessageFormat.format("Entreprise added to department{0}", entrepriseId));
		deptRepoistory.save(dep);
		LOG.info(MessageFormat.format("Department{0}", dep));
        } else {
            LOG.info("Entreprise or Departement doesn't exist");
        }
    }

    public List<String> getAllDepartementsNamesByEntreprise(int entrepriseId) {
        LOG.info(MessageFormat.format(" {0}{1}", ENTREPRISEID, entrepriseId));
        List<String> depNames = new ArrayList<>();
		Entreprise entr = null ;
		Optional<Entreprise> entreprise =entrepriseRepoistory.findById(entrepriseId);
		if (entreprise.isPresent()) {
            entr = entreprise.get();
            LOG.debug(MessageFormat.format("  {0}{1}", ENTREPRISEID, entrepriseId));
            for (Departement dep : entr.getDepartements()) {
    			depNames.add(dep.getName());
    		}
    		LOG.debug(MessageFormat.format("List Of Department Names{0}", depNames));
    		return depNames;
        } else {
        	return Collections.emptyList(); 
        }
    }

    @Transactional
    public void deleteEntrepriseById(int entrepriseId) {
    	Entreprise entreprise = null;
    	LOG.info(MessageFormat.format("Entreprise id {0}", entrepriseId));
    	Optional<Entreprise> entR = entrepriseRepoistory.findById(entrepriseId);
        if (entR.isPresent()) {
        	entreprise = entR.get();
        	entrepriseRepoistory.delete(entreprise);
        	LOG.debug(MessageFormat.format("Entreprise deleted with id {0}", entrepriseId));
        }
    }

    @Transactional
    public void deleteDepartementById(int depId) {
        LOG.info(MessageFormat.format("{0}{1}", DEPARTMENTID, depId));
		Departement dep = null;
		Optional<Departement> department =deptRepoistory.findById(depId);
        if (department.isPresent()) {
            dep = department.get();
            LOG.debug(MessageFormat.format("{0}{1}", DEPARTMENTID, depId));
    		deptRepoistory.delete(dep);
            LOG.debug(MessageFormat.format("Department Id Deleted{0}", depId));

        }
    }

	public Entreprise getEntrepriseById(int entrepriseId) {
		Entreprise entreprise = null;
		LOG.info(MessageFormat.format("Entreprise id {0}", entrepriseId));
		Optional<Entreprise> entR = entrepriseRepoistory.findById(entrepriseId);
		if (entR.isPresent()) {
			entreprise = entR.get();
		}
		LOG.debug(MessageFormat.format("Entreprise {0}", entreprise));
		return entreprise;
	}

}
