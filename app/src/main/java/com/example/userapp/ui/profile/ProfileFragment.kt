package com.example.userapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.userapp.R
import com.example.userapp.SessionManager
import com.example.userapp.data.entity.User
import com.example.userapp.databinding.FragmentProfileBinding
import com.example.userapp.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by activityViewModels()
    private lateinit var sessionManager: SessionManager
    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        // Observa os dados do usuário e preenche os campos
        viewModel.getUserById(userId).observe(viewLifecycleOwner) { user ->
            user?.let {
                currentUser = it
                binding.etName.setText(it.name)
                binding.etEmail.setText(it.email)
                binding.etPhone.setText(it.phone)
                binding.etBirthDate.setText(it.birthDate)
            }
        }

        // Botão Atualizar
        binding.btnUpdate.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val newPassword = binding.etNewPassword.text.toString().trim()
            val birthDate = binding.etBirthDate.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Nome, email e telefone são obrigatórios!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Email inválido"
                return@setOnClickListener
            }

            currentUser?.let { user ->
                val passwordToSave = if (newPassword.isNotEmpty()) {
                    if (newPassword.length < 6) {
                        binding.etNewPassword.error = "Senha deve ter ao menos 6 caracteres"
                        return@setOnClickListener
                    }
                    newPassword
                } else {
                    user.password
                }

                val updatedUser = user.copy(
                    name = name,
                    email = email,
                    phone = phone,
                    password = passwordToSave,
                    birthDate = birthDate
                )

                viewModel.update(updatedUser) {
                    Toast.makeText(requireContext(), "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    binding.etNewPassword.setText("")
                    findNavController().popBackStack()
                }
            }
        }

        // Botão Deletar conta
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Excluir Conta")
                .setMessage("Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.")
                .setPositiveButton("Excluir") { _, _ ->
                    currentUser?.let { user ->
                        viewModel.delete(user) {
                            sessionManager.clearSession()
                            Toast.makeText(requireContext(), "Conta excluída com sucesso.", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
