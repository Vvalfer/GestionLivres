-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 22 fév. 2025 à 21:10
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestionlivres`
--

DELIMITER $$
--
-- Procédures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `generate_data` ()   BEGIN
  DECLARE i INT DEFAULT 1;
  DECLARE total_auteurs INT DEFAULT 20;
  DECLARE total_utilisateurs INT DEFAULT 30;
  DECLARE total_livres INT DEFAULT 50;
  DECLARE total_historiques INT DEFAULT 70;
  DECLARE emprunt_dt DATETIME;
  DECLARE u_prenom CHAR(50);
  DECLARE u_nom CHAR(50);

  /* Insertion dans la table auteur (20 auteurs uniques) */
  SET i = 1;
  WHILE i <= total_auteurs DO
    INSERT INTO auteur(num, nom, prenom, date_naissance, description)
    VALUES (
      CAST(i AS CHAR),
      ELT(((i-1) MOD 20)+1,
           'DUPONT', 'MARTIN', 'LEGRAND', 'DURAND', 'GARNIER',
           'BERNARD', 'MOREAU', 'LEROY', 'ROUSSEAU', 'CLEMENT',
           'LACROIX', 'PERRIN', 'RENAUD', 'VINCENT', 'BONNEAU',
           'LAMBERT', 'FLEURY', 'GUERIN', 'CARON', 'MARCHAND'),
      ELT(((i-1) MOD 20)+1,
           'Jean', 'Sophie', 'Pierre', 'Lucie', 'Paul',
           'Camille', 'Xavier', 'Elise', 'Antoine', 'Isabelle',
           'Marc', 'Claire', 'Julien', 'Marie', 'Louis',
           'Chloé', 'Emilie', 'Olivier', 'Aurélie', 'Guillaume'),
      DATE_ADD('1950-01-01', INTERVAL FLOOR(RAND()*15000) DAY),
      CONCAT('Auteur spécialisé en ',
         ELT(((i-1) MOD 20)+1,
              'science-fiction', 'romans policiers', 'littérature historique', 'livres jeunesse', 'romans contemporains',
              'thrillers psychologiques', 'romans de fantasy', 'dystopies', 'philosophie moderne', 'romans romantiques',
              'littérature sociale', 'biographies captivantes', 'récits de voyage', 'romans d\'aventure', 'sagas familiales',
              'récits historiques', 'contes modernes', 'littérature contemporaine', 'ouvrages poétiques', 'mystères et thrillers')
      )
    );
    SET i = i + 1;
  END WHILE;

  /* Insertion dans la table utilisateur (30 utilisateurs uniques) */
  SET i = 1;
  WHILE i <= total_utilisateurs DO
    SET u_prenom = ELT(((i-1) DIV 20)+1,
           'Alice', 'Thomas');  -- pour 30 utilisateurs, on utilise les 2 premières options
    SET u_nom = ELT(((i-1) MOD 20)+1,
           'Lambert', 'Morel', 'Robert', 'Bertrand', 'Simon',
           'Giraud', 'Fontaine', 'Lemaire', 'Bourgeois', 'Colin',
           'Dubois', 'Renaud', 'Leblanc', 'Gilbert', 'Masson',
           'Martin', 'Poujol', 'Chevalier', 'Lacroix', 'Lafont');
    INSERT INTO utilisateur(email, password, nom, prenom, role, date_inscription)
    VALUES (
      CONCAT(LOWER(u_prenom), '.', LOWER(u_nom), '@example.com'),
      'default_password',
      u_nom,
      u_prenom,
      IF(RAND() < 0.1, 'bibliothecaire', 'adherent'),
      DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND()*30) DAY)
    );
    SET i = i + 1;
  END WHILE;

  /* Insertion dans la table livre (50 livres uniques) */
  SET i = 1;
  WHILE i <= total_livres DO
    INSERT INTO livre(ISBN, titre, prix, adherent_id, auteur)
    VALUES (
      CONCAT('978', LPAD(i, 10, '0')),
      CONCAT(
         ELT(((i-1) MOD 20)+1,
              'Les mystères de', 'Le secret de', 'L\'aventure de', 'L\'odyssée de', 'Les chroniques de',
              'La légende de', 'La quête de', 'Le destin de', 'L\'épopée de', 'Les récits de',
              'Les ombres de', 'Les mémoires de', 'Les vestiges de', 'Les murmures de', 'Les énigmes de',
              'Les trésors de', 'Les visions de', 'Les échos de', 'Les promesses de', 'Les rêves de'),
         ' ',
         ELT(((i-1) DIV 20)+1,
              'Paris', 'Londres', 'New York', 'Tokyo', 'Rome',
              'la vie', 'l\'âme', 'les ombres', 'le temps', 'l\'inconnu',
              'le destin', 'la passion', 'les étoiles', 'le mystère', 'la forêt',
              'le désert', 'la mer', 'les montagnes', 'les cieux', 'le crépuscule')
      ),
      ROUND(10 + RAND()*40, 2),
      IF(RAND() < 0.2, FLOOR(1 + RAND()*total_utilisateurs), NULL),
      CAST(FLOOR(1 + RAND()*total_auteurs) AS CHAR)
    );
    SET i = i + 1;
  END WHILE;

  /* Insertion dans la table historique (70 enregistrements estimés) */
  SET i = 1;
  WHILE i <= total_historiques DO
    SET emprunt_dt = DATE_ADD('2025-01-01 00:00:00', INTERVAL FLOOR(RAND()*3000000) SECOND);
    INSERT INTO historique(isbn, adherent_id, date_emprunt, date_retour)
    VALUES (
      CONCAT('978', LPAD(FLOOR(1 + RAND()*total_livres), 10, '0')),
      FLOOR(1 + RAND()*total_utilisateurs),
      emprunt_dt,
      IF(RAND() < 0.7, DATE_ADD(emprunt_dt, INTERVAL FLOOR(RAND()*604800) SECOND), NULL)
    );
    SET i = i + 1;
  END WHILE;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `auteur`
--

CREATE TABLE `auteur` (
  `num` varchar(20) NOT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `auteur`
--

INSERT INTO `auteur` (`num`, `nom`, `prenom`, `date_naissance`, `description`) VALUES
('1', 'DUPONT', 'Jean', '1980-11-02', 'Auteur spécialisé en science-fiction'),
('10', 'CLEMENT', 'Isabelle', '1983-10-06', 'Auteur spécialisé en romans romantiques'),
('11', 'LACROIX', 'Marc', '1971-03-31', 'Auteur spécialisé en littérature sociale'),
('12', 'PERRIN', 'Claire', '1954-12-06', 'Auteur spécialisé en biographies captivantes'),
('13', 'RENAUD', 'Julien', '1951-12-27', 'Auteur spécialisé en récits de voyage'),
('14', 'VINCENT', 'Marie', '1986-03-21', 'Auteur spécialisé en romans d\'aventure'),
('15', 'BONNEAU', 'Louis', '1960-11-06', 'Auteur spécialisé en sagas familiales'),
('16', 'LAMBERT', 'Chloé', '1977-09-24', 'Auteur spécialisé en récits historiques'),
('17', 'FLEURY', 'Emilie', '1973-12-21', 'Auteur spécialisé en contes modernes'),
('18', 'GUERIN', 'Olivier', '1986-08-30', 'Auteur spécialisé en littérature contemporaine'),
('19', 'CARON', 'Aurélie', '1979-04-03', 'Auteur spécialisé en ouvrages poétiques'),
('2', 'MARTIN', 'Sophie', '1968-02-15', 'Auteur spécialisé en romans policiers'),
('20', 'MARCHAND', 'Guillaume', '1986-04-15', 'Auteur spécialisé en mystères et thrillers'),
('3', 'LEGRAND', 'Pierre', '1989-03-03', 'Auteur spécialisé en littérature historique'),
('4', 'DURAND', 'Lucie', '1968-04-10', 'Auteur spécialisé en livres jeunesse'),
('5', 'GARNIER', 'Paul', '1964-12-05', 'Auteur spécialisé en romans contemporains'),
('6', 'BERNARD', 'Camille', '1969-10-28', 'Auteur spécialisé en thrillers psychologiques'),
('7', 'MOREAU', 'Xavier', '1963-04-07', 'Auteur spécialisé en romans de fantasy'),
('8', 'LEROY', 'Elise', '1956-11-04', 'Auteur spécialisé en dystopies'),
('9', 'ROUSSEAU', 'Antoine', '1985-07-02', 'Auteur spécialisé en philosophie moderne');

-- --------------------------------------------------------

--
-- Structure de la table `historique`
--

CREATE TABLE `historique` (
  `id` int(11) NOT NULL,
  `isbn` varchar(13) NOT NULL,
  `adherent_id` int(11) NOT NULL,
  `date_emprunt` timestamp NOT NULL DEFAULT current_timestamp(),
  `date_retour` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `historique`
--

INSERT INTO `historique` (`id`, `isbn`, `adherent_id`, `date_emprunt`, `date_retour`) VALUES
(1, '9780000000036', 21, '2025-01-10 22:04:35', '2025-01-12 04:10:44'),
(2, '9780000000018', 8, '2025-01-07 04:32:16', '2025-01-09 11:21:07'),
(3, '9780000000043', 12, '2025-02-03 13:14:42', '2025-02-08 13:35:51'),
(4, '9780000000005', 4, '2025-01-16 12:59:13', '2025-01-19 05:21:00'),
(5, '9780000000007', 3, '2025-01-30 16:31:33', '2025-01-30 18:23:55'),
(6, '9780000000020', 10, '2025-01-31 17:38:21', '2025-02-03 03:56:03'),
(7, '9780000000030', 1, '2025-01-12 08:01:36', '2025-01-13 02:11:20'),
(8, '9780000000002', 16, '2025-01-31 04:14:54', '2025-02-01 03:54:55'),
(9, '9780000000048', 17, '2025-01-03 13:29:30', NULL),
(10, '9780000000007', 3, '2025-01-19 01:28:14', NULL),
(11, '9780000000040', 20, '2025-01-27 03:19:29', NULL),
(12, '9780000000018', 19, '2025-01-14 10:18:37', NULL),
(13, '9780000000006', 18, '2025-01-01 00:08:33', '2025-01-01 07:37:30'),
(14, '9780000000031', 12, '2025-01-20 00:04:06', '2025-01-24 10:48:27'),
(15, '9780000000025', 12, '2025-01-24 12:11:35', '2025-01-26 18:10:59'),
(16, '9780000000030', 19, '2025-01-04 19:08:04', '2025-01-09 04:52:19'),
(17, '9780000000023', 14, '2025-01-10 06:22:50', NULL),
(18, '9780000000026', 25, '2025-01-09 22:44:19', '2025-01-10 13:05:26'),
(19, '9780000000017', 26, '2025-02-01 17:54:02', '2025-02-02 08:05:06'),
(20, '9780000000044', 3, '2025-01-15 17:11:54', NULL),
(21, '9780000000024', 1, '2025-01-27 22:58:38', '2025-01-29 21:54:54'),
(22, '9780000000009', 21, '2025-01-14 22:51:45', NULL),
(23, '9780000000027', 30, '2025-01-08 09:38:31', '2025-01-14 14:02:00'),
(24, '9780000000044', 14, '2025-01-11 12:29:48', '2025-01-16 06:50:10'),
(25, '9780000000048', 28, '2025-01-21 16:34:03', NULL),
(26, '9780000000039', 5, '2025-01-09 04:39:12', '2025-01-16 03:04:36'),
(27, '9780000000018', 10, '2025-01-17 00:31:25', '2025-01-23 05:59:27'),
(28, '9780000000002', 29, '2025-01-26 16:37:18', '2025-01-30 16:04:04'),
(29, '9780000000008', 14, '2025-01-27 18:16:20', NULL),
(30, '9780000000025', 3, '2025-01-28 18:08:15', NULL),
(31, '9780000000039', 21, '2025-01-03 07:31:23', '2025-01-06 05:56:28'),
(32, '9780000000040', 16, '2025-01-29 01:55:05', '2025-02-03 14:42:32'),
(33, '9780000000013', 26, '2025-01-05 05:27:55', '2025-01-05 08:43:55'),
(34, '9780000000037', 30, '2025-01-20 07:51:17', NULL),
(35, '9780000000046', 23, '2025-01-22 02:22:27', NULL),
(36, '9780000000017', 21, '2025-01-23 13:49:42', '2025-01-24 08:32:55'),
(37, '9780000000049', 3, '2025-01-10 00:39:18', '2025-01-10 11:55:36'),
(38, '9780000000031', 4, '2025-02-03 07:44:48', NULL),
(39, '9780000000043', 19, '2025-01-31 16:26:47', '2025-02-05 14:07:21'),
(40, '9780000000040', 30, '2025-02-03 21:05:19', '2025-02-10 08:31:39'),
(41, '9780000000036', 27, '2025-02-01 03:09:53', '2025-02-04 16:30:15'),
(42, '9780000000040', 12, '2025-01-30 22:38:43', '2025-02-02 22:48:19'),
(43, '9780000000038', 28, '2025-01-22 01:22:04', '2025-01-22 20:00:27'),
(44, '9780000000040', 20, '2025-01-15 15:16:47', NULL),
(45, '9780000000044', 17, '2025-01-10 14:36:58', '2025-01-11 06:00:48'),
(46, '9780000000037', 19, '2025-02-04 16:05:16', NULL),
(47, '9780000000002', 26, '2025-01-27 07:36:58', '2025-01-30 04:16:36'),
(48, '9780000000004', 30, '2025-01-16 22:02:09', '2025-01-20 12:42:25'),
(49, '9780000000049', 10, '2025-01-18 10:03:16', NULL),
(50, '9780000000002', 8, '2025-01-23 04:34:29', '2025-01-29 09:38:14'),
(51, '9780000000033', 3, '2025-01-03 03:55:24', '2025-01-03 20:48:47'),
(52, '9780000000006', 11, '2025-01-03 20:57:12', '2025-01-08 15:18:44'),
(53, '9780000000045', 9, '2025-01-14 08:08:44', '2025-01-18 15:39:46'),
(54, '9780000000015', 13, '2025-01-01 23:33:47', '2025-01-05 02:27:02'),
(55, '9780000000041', 18, '2025-01-29 19:21:01', '2025-02-04 03:43:58'),
(56, '9780000000010', 3, '2025-01-11 06:50:34', NULL),
(57, '9780000000040', 13, '2025-01-30 10:47:59', '2025-02-01 03:08:16'),
(58, '9780000000042', 25, '2025-01-04 18:16:49', '2025-01-07 08:22:55'),
(59, '9780000000038', 8, '2025-01-06 22:07:22', NULL),
(60, '9780000000036', 27, '2025-01-31 23:26:08', '2025-02-04 11:07:51'),
(61, '9780000000032', 21, '2025-01-29 14:42:55', '2025-02-02 10:39:01'),
(62, '9780000000013', 22, '2025-01-07 03:52:57', NULL),
(63, '9780000000050', 18, '2025-01-04 18:38:54', '2025-01-06 12:49:50'),
(64, '9780000000023', 16, '2025-01-09 06:52:40', '2025-01-13 01:40:22'),
(65, '9780000000034', 6, '2025-01-03 00:15:00', NULL),
(66, '9780000000045', 1, '2025-01-05 23:36:01', '2025-01-11 22:44:54'),
(67, '9780000000006', 6, '2025-01-05 15:03:47', '2025-01-06 17:56:48'),
(68, '9780000000023', 20, '2025-01-07 09:00:29', NULL),
(69, '9780000000046', 20, '2025-02-04 00:09:35', '2025-02-06 14:53:34'),
(70, '9780000000016', 4, '2025-01-17 21:07:01', '2025-01-24 04:19:03');

-- --------------------------------------------------------

--
-- Structure de la table `livre`
--

CREATE TABLE `livre` (
  `ISBN` varchar(20) NOT NULL,
  `titre` varchar(50) NOT NULL,
  `prix` float DEFAULT NULL,
  `adherent_id` int(11) DEFAULT NULL,
  `auteur` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `livre`
--

INSERT INTO `livre` (`ISBN`, `titre`, `prix`, `adherent_id`, `auteur`) VALUES
('9780000000001', 'Les mystères de Paris', 39.67, NULL, '6'),
('9780000000002', 'Le Bruit et la Fureur', 33.31, 10, '12'),
('9780000000003', 'Demande à la poussière', 48.65, 13, '19'),
('9780000000004', 'Voyage au bout de la nuit', 19.93, NULL, '20'),
('9780000000005', 'La Divine Comédie', 15.69, NULL, '17'),
('9780000000006', 'La légende de Paris', 36.78, NULL, '1'),
('9780000000007', 'Au coeur des ténèbres', 38.83, NULL, '9'),
('9780000000008', 'Le Grand Nulle Part', 33.33, NULL, '9'),
('9780000000009', 'Les Âmes mortes', 17.93, NULL, '2'),
('9780000000010', 'La Proie et l\'ombre', 14.38, NULL, '11'),
('9780000000011', 'L\'étrange défaite', 26.48, NULL, '11'),
('9780000000012', 'Sanctuaire', 10.05, NULL, '1'),
('9780000000013', 'Par-delà le bien et le mal', 48.83, NULL, '16'),
('9780000000014', 'Les raisins de la colère', 34.99, NULL, '5'),
('9780000000015', 'La mélancolie de la résistance', 40.75, 6, '13'),
('9780000000016', 'Cent ans de solitude', 33.5, 16, '8'),
('9780000000017', ' Les frères Karamazov Tome 1', 21.2, NULL, '16'),
('9780000000018', 'Tes mots sur mes lèvres', 48.78, NULL, '10'),
('9780000000019', ' Nos étoiles contraires', 49.96, NULL, '14'),
('9780000000020', 'Les rêves de Paris', 39.37, NULL, '4'),
('9780000000021', 'La Mécanique du cœur', 40.84, NULL, '12'),
('9780000000022', 'Le secret de Londres', 44.03, NULL, '15'),
('9780000000023', ' La Voleuse de livres', 19.84, 18, '15'),
('9780000000024', 'Ce qu\'ils n\'ont pas pu nous prendre', 42.93, NULL, '9'),
('9780000000025', 'La Fille de papier', 15.27, NULL, '15'),
('9780000000026', 'La légende de Londres', 22.28, NULL, '1'),
('9780000000027', 'La quête de Londres', 10.03, NULL, '10'),
('9780000000028', 'Le destin de Londres', 41.12, NULL, '14'),
('9780000000029', 'L\'épopée de Londres', 18.59, 14, '4'),
('9780000000030', 'Les récits de Londres', 32.26, NULL, '12'),
('9780000000031', 'Les ombres de Londres', 11.1, NULL, '7'),
('9780000000032', 'Les mémoires de Londres', 17.3, NULL, '3'),
('9780000000033', 'Les vestiges de Londres', 44.55, NULL, '20'),
('9780000000034', 'Les murmures de Londres', 16.38, NULL, '17'),
('9780000000035', 'Les énigmes de Londres', 27.82, NULL, '16'),
('9780000000036', 'Les trésors de Londres', 23.13, NULL, '18'),
('9780000000037', 'Les visions de Londres', 19.67, NULL, '6'),
('9780000000038', 'Les échos de Londres', 29.31, NULL, '17'),
('9780000000039', 'Les promesses de Londres', 13.28, NULL, '15'),
('9780000000040', 'Les rêves de Londres', 31.41, NULL, '6'),
('9780000000041', 'Les mystères de New York', 33.86, 6, '6'),
('9780000000042', 'Le secret de New York', 42.42, NULL, '18'),
('9780000000043', 'L\'aventure de New York', 36.76, NULL, '7'),
('9780000000044', 'L\'odyssée de New York', 35.91, NULL, '7'),
('9780000000045', 'Les chroniques de New York', 40.28, NULL, '3'),
('9780000000046', 'La légende de New York', 12.57, NULL, '6'),
('9780000000047', 'La quête de New York', 38.01, NULL, '6'),
('9780000000048', 'Le destin de New York', 21.28, NULL, '7'),
('9780000000049', 'L\'épopée de New York', 35.97, NULL, '14'),
('9780000000050', 'Les récits de New York', 22.91, NULL, '5');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `role` enum('bibliothecaire','adherent') NOT NULL,
  `date_inscription` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `email`, `password`, `nom`, `prenom`, `role`, `date_inscription`) VALUES
(1, 'alice.lambert@example.com', 'default_password', 'Lambert', 'Alice', 'adherent', '2025-01-22 23:00:00'),
(2, 'bertrand.morel@example.com', 'default_password', 'Morel', 'Bertrand', 'adherent', '2025-01-11 23:00:00'),
(3, 'michel.robert@example.com', 'default_password', 'Robert', 'Michel', 'bibliothecaire', '2025-01-05 23:00:00'),
(4, 'benjamin.bertrand@example.com', 'default_password', 'Bertrand', 'Benjamin', 'adherent', '2025-01-08 23:00:00'),
(5, 'luc.simon@example.com', 'default_password', 'Simon', 'Luc', 'adherent', '2025-01-26 23:00:00'),
(6, 'philipe.giraud@example.com', 'default_password', 'Giraud', 'Philipe', 'adherent', '2025-01-26 23:00:00'),
(7, 'sophie.fontaine@example.com', 'default_password', 'Fontaine', 'Sophie', 'adherent', '2025-01-06 23:00:00'),
(8, 'christian.lemaire@example.com', 'default_password', 'Lemaire', 'Christian', 'adherent', '2025-01-28 23:00:00'),
(9, 'frantz.bourgeois@example.com', 'default_password', 'Bourgeois', 'Frantz', 'adherent', '2024-12-31 23:00:00'),
(10, 'lucas.colin@example.com', 'default_password', 'Colin', 'Lucas', 'adherent', '2024-12-31 23:00:00'),
(11, 'frederic.dubois@example.com', 'default_password', 'Dubois', 'Frederic', 'adherent', '2025-01-08 23:00:00'),
(12, 'robert.renaud@example.com', 'default_password', 'Renaud', 'Robert', 'adherent', '2025-01-02 23:00:00'),
(13, 'nathalie.leblanc@example.com', 'default_password', 'Leblanc', 'Nathalie', 'bibliothecaire', '2024-12-31 23:00:00'),
(14, 'stella.gilbert@example.com', 'default_password', 'Gilbert', 'Stella', 'adherent', '2025-01-25 23:00:00'),
(15, 'marylin.masson@example.com', 'default_password', 'Masson', 'Marylin', 'adherent', '2025-01-27 23:00:00'),
(16, 'yasmine.martin@example.com', 'default_password', 'Martin', 'Yasmine', 'adherent', '2025-01-29 23:00:00'),
(17, 'norbert.poujol@example.com', 'default_password', 'Poujol', 'Norbert', 'adherent', '2025-01-10 23:00:00'),
(18, 'alice.chevalier@example.com', 'default_password', 'Chevalier', 'Alice', 'adherent', '2025-01-21 23:00:00'),
(19, 'geoffroy.lacroix@example.com', 'default_password', 'Lacroix', 'Geoffroy', 'adherent', '2025-01-08 23:00:00'),
(20, 'mickael.lafont@example.com', 'default_password', 'Lafont', 'Mickael', 'adherent', '2025-01-27 23:00:00'),
(21, 'thomas.lambert@example.com', 'default_password', 'Lambert', 'Thomas', 'adherent', '2025-01-24 23:00:00'),
(22, 'tristan.morel@example.com', 'default_password', 'Morel', 'Tristan', 'adherent', '2025-01-08 23:00:00'),
(23, 'eric.robert@example.com', 'default_password', 'Robert', 'Eric', 'adherent', '2025-01-23 23:00:00'),
(24, 'maximilien.bertrand@example.com', 'default_password', 'Bertrand', 'Maximilien', 'bibliothecaire', '2025-01-20 23:00:00'),
(25, 'george.simon@example.com', 'default_password', 'Simon', 'George', 'adherent', '2025-01-19 23:00:00'),
(26, 'thomas.giraud@example.com', 'default_password', 'Giraud', 'Thomas', 'adherent', '2025-01-14 23:00:00'),
(27, 'thomas.fontaine@example.com', 'default_password', 'Fontaine', 'Thomas', 'adherent', '2025-01-05 23:00:00'),
(28, 'thomas.lemaire@example.com', 'default_password', 'Lemaire', 'Thomas', 'adherent', '2025-01-05 23:00:00'),
(29, 'thomas.bourgeois@example.com', 'default_password', 'Bourgeois', 'Thomas', 'adherent', '2025-01-01 23:00:00'),
(30, 'thomas.colin@example.com', 'default_password', 'Colin', 'Thomas', 'adherent', '2025-01-14 23:00:00');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `auteur`
--
ALTER TABLE `auteur`
  ADD PRIMARY KEY (`num`);

--
-- Index pour la table `historique`
--
ALTER TABLE `historique`
  ADD PRIMARY KEY (`id`),
  ADD KEY `isbn` (`isbn`),
  ADD KEY `historique_ibfk_2` (`adherent_id`);

--
-- Index pour la table `livre`
--
ALTER TABLE `livre`
  ADD PRIMARY KEY (`ISBN`),
  ADD KEY `FK1` (`adherent_id`),
  ADD KEY `FK2` (`auteur`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `historique`
--
ALTER TABLE `historique`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `historique`
--
ALTER TABLE `historique`
  ADD CONSTRAINT `historique_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `livre` (`ISBN`),
  ADD CONSTRAINT `historique_ibfk_2` FOREIGN KEY (`adherent_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `livre`
--
ALTER TABLE `livre`
  ADD CONSTRAINT `FK2` FOREIGN KEY (`auteur`) REFERENCES `auteur` (`num`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
