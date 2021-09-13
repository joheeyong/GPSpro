package travel.android.gpspro.DB.ViewModel;


import java.util.ArrayList;
import java.util.List;

import travel.android.gpspro.DB.Dao.ContactRepository;
import travel.android.gpspro.DB.Entity.Contact;

public class ContactService {

    private ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }

    public long insert(Contact contact){
        return contactRepository.save(contact);
    }

    public int update(Contact contact){
        return contactRepository.update(contact);
    }

    public int delete(long contactId){
        return contactRepository.delete(contactId);
    }

    public int alldelete(){
        return contactRepository.deleteAll();
    }

    public Contact proview(long contactId){
        return contactRepository.findById(contactId);
    }

    public List<Contact> allview(int idd){
        List<Contact> contacts = contactRepository.findAlll(idd);
        if(contacts == null){
            return new ArrayList<> ();
        }
        return contactRepository.findAlll(idd);
    }

}



